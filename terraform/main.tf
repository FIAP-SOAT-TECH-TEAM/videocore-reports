module "helm" {
  source = "./modules/helm"

  subscription_id                   = var.subscription_id
  videocore-backend-container       = var.videocore-backend-container
  videocore-backend-infra-key       = var.videocore-backend-infra-key
  videocore-backend-resource-group  = var.videocore-backend-resource-group
  videocore-backend-storage-account = var.videocore-backend-storage-account
  release_name                      = var.release_name
  repository_url                    = local.repository_url
  chart_name                        = var.chart_name
  chart_version                     = var.chart_version
  release_namespace                 = var.release_namespace
  docker_image_uri                  = var.docker_image_uri
  docker_image_tag                  = var.docker_image_tag
  api_ingress_path                  = var.api_ingress_path
  api_websocket_base_endpoint       = var.ws_url_template
}

module "apim" {
  source = "./modules/apim"

  subscription_id                     = var.subscription_id
  videocore-backend-container         = var.videocore-backend-container
  videocore-backend-infra-key         = var.videocore-backend-infra-key
  videocore-backend-auth-key          = var.videocore-backend-auth-key
  videocore-backend-resource-group    = var.videocore-backend-resource-group
  videocore-backend-storage-account   = var.videocore-backend-storage-account
  apim_api_name                       = var.apim_api_name
  apim_api_version                    = var.apim_api_version
  apim_display_name                   = var.apim_display_name
  swagger_path                        = var.swagger_path
  api_ingress_path                    = local.api_ingress_path_without_slash
  apim_ws_api_name                    = var.apim_ws_api_name
  apim_ws_api_version                 = var.apim_ws_api_version
  apim_ws_display_name                = var.apim_ws_display_name
  api_reports_private_dns_fqdn        = var.api_reports_private_dns_fqdn

  depends_on = [module.helm]
}