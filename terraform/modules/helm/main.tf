resource "helm_release" "videocore_reports" {
  name            = var.release_name
  repository      = var.repository_url
  chart           = var.chart_name
  version         = var.chart_version
  namespace       = var.release_namespace

  # Permitir upgrade e reinstalação do release automaticamente (apenas para fins da atividade)
  upgrade_install = true
  force_update    = true

  set {
    name  = "namespace.api.name"
    value = data.terraform_remote_state.infra.outputs.aks_reports_namespace_name
  }

  set {
    name  = "namespace.monitor.name"
    value = data.terraform_remote_state.infra.outputs.aks_monitor_namespace_name
  }

  set {
    name  = "ingress.hosts[0].host"
    value = data.terraform_remote_state.infra.outputs.api_reports_private_dns_fqdn
  }

  set {
    name  = "api.image.repository"
    value = var.docker_image_uri
  }

  set {
    name  = "api.image.tag"
    value = var.docker_image_tag
  }

  set {
    name  = "api.ingress.path"
    value = var.api_ingress_path
  }

  set {
    name  = "api.websocket.baseEndpoint"
    value = var.api_websocket_base_endpoint
  }

  set {
    name  = "api.cors.allowedOrigins"
    value = data.terraform_remote_state.infra.outputs.cloudfront_url
  }

  set {
    name  = "secrets.azureKeyVault.keyVaultName"
    value = data.terraform_remote_state.infra.outputs.akv_name
  }

  set {
    name  = "secrets.azureKeyVault.tenantId"
    value = data.terraform_remote_state.infra.outputs.tenant_id
  }

  set {
    name  = "secrets.azureKeyVault.clientID"
    value = data.terraform_remote_state.infra.outputs.aks_secret_identity_client_id
  }

}