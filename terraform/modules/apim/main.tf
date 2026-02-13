resource "azurerm_api_management_api" "videocoreapi_apim" {
  name                = var.apim_api_name
  resource_group_name = data.terraform_remote_state.infra.outputs.apim_resource_group
  api_management_name = data.terraform_remote_state.infra.outputs.apim_name
  revision            = var.apim_api_version
  display_name        = var.apim_display_name
  path                = var.api_ingress_path
  protocols           = ["https", "wss"]

  import {
    content_format = "openapi+json"
    content_value  = file(var.swagger_path)
  }
}

resource "azurerm_api_management_api" "videocoreapi_ws_apim" {
  name                = var.apim_ws_api_name
  resource_group_name = data.terraform_remote_state.infra.outputs.apim_resource_group
  api_management_name = data.terraform_remote_state.infra.outputs.apim_name
  revision            = var.apim_ws_api_version
  display_name        = var.apim_ws_display_name
  path                = "${var.api_ingress_path}/ws"
  service_url         = "ws://${data.terraform_remote_state.infra.outputs.api_reports_private_dns_fqdn}/${var.api_ingress_path}"
  protocols           = ["wss"]
  api_type            = "websocket"
}

resource "azurerm_api_management_api_policy" "set_backend_api" {
  api_name            = azurerm_api_management_api.videocoreapi_apim.name
  api_management_name = data.terraform_remote_state.infra.outputs.apim_name
  resource_group_name = data.terraform_remote_state.infra.outputs.apim_resource_group

  xml_content = <<XML
  <policies>
    <inbound>
      <base /> <!-- Herda as policies globais configuradas no API Management -->

      <!-- Extrai token -->
      <set-variable name="bearerToken" value="@(context.Request.Headers.GetValueOrDefault("Authorization", "").Split(' ').Last())" />

      <!-- Validação imediata: se vazio, retorna 401 -->
      <choose>
        <when condition="@(string.IsNullOrEmpty((string)context.Variables["bearerToken"]))">
            <return-response>
                <set-status code="401" reason="Unauthorized" />
                <set-header name="Content-Type" exists-action="override">
                    <value>application/json</value>
                </set-header>
                <set-body>@{
                    return new JObject(
                        new JProperty("timestamp", DateTime.UtcNow.ToString("o")),
                        new JProperty("status", 401),
                        new JProperty("message", "Bearer token está ausente ou vazio"),
                        new JProperty("path", context.Request.OriginalUrl.Path)
                    ).ToString();
                }</set-body>
            </return-response>
        </when>
      </choose>

      <!-- Normaliza Path -->
      <set-variable name="normalizedPath" value="@{
          var path = context.Request.OriginalUrl?.Path ?? "";

          // Remove barra final se existir e não for apenas "/"
          if (path.Length > 1 && path.EndsWith("/"))
          {
              path = path.TrimEnd('/');
          }

          return path;
      }" />

      <!-- Valida token -->
      <send-request mode="new" response-variable-name="authResponse" timeout="10">
        <set-url>@($"${data.terraform_remote_state.azfunc.outputs.auth_api_validate_endpoint}?access_token={context.Variables["bearerToken"]}&url={context.Variables["normalizedPath"]}&http_method={context.Operation.Method}")</set-url>
        <set-method>GET</set-method>
      </send-request>

      <!-- Retorna authResponse se não for 200 -->
      <choose>
        <when condition="@(context.Variables.GetValueOrDefault<IResponse>("authResponse")?.StatusCode != 200)">
          <return-response response-variable-name="authResponse" />
        </when>
      </choose>

      <!-- Converte body da auth API para JObject -->
      <set-variable name="authBody" value="@(((IResponse)context.Variables["authResponse"]).Body.As<JObject>(true))" />

      <!-- Aplica headers ao backend -->
      <set-header name="Auth-Subject" exists-action="override">
        <value>@(context.Variables.GetValueOrDefault<JObject>("authBody")?["subject"]?.ToString())</value>
      </set-header>
      <set-header name="Auth-Name" exists-action="override">
        <value>@(context.Variables.GetValueOrDefault<JObject>("authBody")?["name"]?.ToString())</value>
      </set-header>
      <set-header name="Auth-Email" exists-action="override">
        <value>@(context.Variables.GetValueOrDefault<JObject>("authBody")?["email"]?.ToString())</value>
      </set-header>

      <!-- Define o backend real -->
      <set-backend-service base-url="http://${data.terraform_remote_state.infra.outputs.api_reports_private_dns_fqdn}/${var.api_ingress_path}" />
    </inbound>

    <backend>
      <base /> <!-- Herda policies globais aplicadas ao backend -->
    </backend>

    <outbound>
      <base /> <!-- Herda policies globais aplicadas à resposta antes de retornar ao cliente -->
    </outbound>

    <on-error>
      <!-- Normaliza Path -->
      <set-variable name="normalizedPath" value="@{
          var path = context.Request.OriginalUrl?.Path ?? "";

          // Remove barra final se existir e não for apenas "/"
          if (path.Length > 1 && path.EndsWith("/"))
          {
              path = path.TrimEnd('/');
          }

          return path;
      }" />
      <choose>
        <when condition="@(context.LastError != null)">
          <return-response>
            <set-status code="@(context.Response?.StatusCode ?? 500)" reason="Other errors" />
            <set-header name="Content-Type" exists-action="override">
              <value>application/json</value>
            </set-header>
            <set-body>@{
                var error = new JObject();
                error["timestamp"] = DateTime.UtcNow.ToString("o"); // ISO 8601
                error["status"]    = context.Response?.StatusCode ?? 500;
                error["message"]   = context.LastError.Message;
                error["path"]      = context.Variables.GetValueOrDefault<string>("normalizedPath");
                return error.ToString(Newtonsoft.Json.Formatting.Indented);
            }</set-body>
          </return-response>
        </when>
      </choose>
    </on-error>

  </policies>
  XML
}


resource "azurerm_api_management_api_policy" "set_ws_backend_api" {
  api_name            = azurerm_api_management_api.videocoreapi_ws_apim
  api_management_name = data.terraform_remote_state.infra.outputs.apim_name
  resource_group_name = data.terraform_remote_state.infra.outputs.apim_resource_group

  xml_content = <<XML
  <policies>
    <inbound>
      <base /> <!-- Herda as policies globais configuradas no API Management -->

      <!-- Extrai token -->
      <set-variable name="bearerToken" value="@(context.Request.Headers.GetValueOrDefault("Authorization", "").Split(' ').Last())" />

      <!-- Validação imediata: se vazio, retorna 401 -->
      <choose>
        <when condition="@(string.IsNullOrEmpty((string)context.Variables["bearerToken"]))">
            <return-response>
                <set-status code="401" reason="Unauthorized" />
                <set-header name="Content-Type" exists-action="override">
                    <value>application/json</value>
                </set-header>
                <set-body>@{
                    return new JObject(
                        new JProperty("timestamp", DateTime.UtcNow.ToString("o")),
                        new JProperty("status", 401),
                        new JProperty("message", "Bearer token está ausente ou vazio"),
                        new JProperty("path", context.Request.OriginalUrl.Path)
                    ).ToString();
                }</set-body>
            </return-response>
        </when>
      </choose>

      <!-- Normaliza Path -->
      <set-variable name="normalizedPath" value="@{
          var path = context.Request.OriginalUrl?.Path ?? "";

          // Remove barra final se existir e não for apenas "/"
          if (path.Length > 1 && path.EndsWith("/"))
          {
              path = path.TrimEnd('/');
          }

          return path;
      }" />

      <!-- Valida token -->
      <send-request mode="new" response-variable-name="authResponse" timeout="10">
        <set-url>@($"${data.terraform_remote_state.azfunc.outputs.auth_api_validate_endpoint}?access_token={context.Variables["bearerToken"]}&url={context.Variables["normalizedPath"]}&http_method={context.Operation.Method}")</set-url>
        <set-method>GET</set-method>
      </send-request>

      <!-- Retorna authResponse se não for 200 -->
      <choose>
        <when condition="@(context.Variables.GetValueOrDefault<IResponse>("authResponse")?.StatusCode != 200)">
          <return-response response-variable-name="authResponse" />
        </when>
      </choose>

      <!-- Converte body da auth API para JObject -->
      <set-variable name="authBody" value="@(((IResponse)context.Variables["authResponse"]).Body.As<JObject>(true))" />

      <!-- Aplica headers ao backend -->
      <set-header name="Auth-Subject" exists-action="override">
        <value>@(context.Variables.GetValueOrDefault<JObject>("authBody")?["subject"]?.ToString())</value>
      </set-header>
      <set-header name="Auth-Name" exists-action="override">
        <value>@(context.Variables.GetValueOrDefault<JObject>("authBody")?["name"]?.ToString())</value>
      </set-header>
      <set-header name="Auth-Email" exists-action="override">
        <value>@(context.Variables.GetValueOrDefault<JObject>("authBody")?["email"]?.ToString())</value>
      </set-header>
    </inbound>

    <backend>
      <base /> <!-- Herda policies globais aplicadas ao backend -->
    </backend>

    <outbound>
      <base /> <!-- Herda policies globais aplicadas à resposta antes de retornar ao cliente -->
    </outbound>

    <on-error>
      <!-- Normaliza Path -->
      <set-variable name="normalizedPath" value="@{
          var path = context.Request.OriginalUrl?.Path ?? "";

          // Remove barra final se existir e não for apenas "/"
          if (path.Length > 1 && path.EndsWith("/"))
          {
              path = path.TrimEnd('/');
          }

          return path;
      }" />
      <choose>
        <when condition="@(context.LastError != null)">
          <return-response>
            <set-status code="@(context.Response?.StatusCode ?? 500)" reason="Other errors" />
            <set-header name="Content-Type" exists-action="override">
              <value>application/json</value>
            </set-header>
            <set-body>@{
                var error = new JObject();
                error["timestamp"] = DateTime.UtcNow.ToString("o"); // ISO 8601
                error["status"]    = context.Response?.StatusCode ?? 500;
                error["message"]   = context.LastError.Message;
                error["path"]      = context.Variables.GetValueOrDefault<string>("normalizedPath");
                return error.ToString(Newtonsoft.Json.Formatting.Indented);
            }</set-body>
          </return-response>
        </when>
      </choose>
    </on-error>

  </policies>
  XML
}


resource "azurerm_api_management_product_api" "videocoreapi_start_product_assoc" {
  api_name            = azurerm_api_management_api.videocoreapi_apim.name
  product_id          = data.terraform_remote_state.infra.outputs.apim_videocore_start_productid
  api_management_name = data.terraform_remote_state.infra.outputs.apim_name
  resource_group_name = data.terraform_remote_state.infra.outputs.apim_resource_group
}

resource "azurerm_api_management_product_api" "videocoreapi_ws_start_product_assoc" {
  api_name            = azurerm_api_management_api.videocoreapi_ws_apim.name
  product_id          = data.terraform_remote_state.infra.outputs.apim_videocore_start_productid
  api_management_name = data.terraform_remote_state.infra.outputs.apim_name
  resource_group_name = data.terraform_remote_state.infra.outputs.apim_resource_group
}