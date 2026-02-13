# Commn
  variable "subscription_id" {
    type        = string
    description = "Azure Subscription ID"
  }

  variable "api_reports_private_dns_fqdn" {
    type = string
    description = "FQDN do registro A do microsserviço de reports na zona DNS privada"
  }

# remote states
  variable "videocore-backend-resource-group" {
    type        = string
    description = "Nome do resource group onde o backend está armazenado"
  }

  variable "videocore-backend-storage-account" {
    type        = string
    description = "Nome da conta de armazenamento onde o backend está armazenado"
  }

  variable "videocore-backend-container" {
    type        = string
    description = "Nome do contêiner onde o backend está armazenado"
  }

  variable "videocore-backend-infra-key" {
    type        = string
    description = "Chave do arquivo tfstate do videocore-infra"
  }

  variable "videocore-backend-auth-key" {
    type        = string
    description = "Chave do arquivo tfstate do videocore-auth"
  }

# HELM

  variable "release_name" {
    type        = string
    description = "Nome do release do Helm."
    default = "videocore-reports"
  }

  variable "release_namespace" {
    type        = string
    description = "Namespace Kubernetes onde o release será instalado."
    default     = "default"
  }

  variable "chart_name" {
    type        = string
    description = "Nome do chart Helm a ser instalado."
  }

  variable "chart_version" {
    type        = string
    description = "Versão do chart Helm."
  }

  variable "docker_image_uri" {
    type        = string
    description = "URI da imagem Docker a ser utilizada."
  }

  variable "docker_image_tag" {
    type        = string
    description = "Tag da imagem Docker a ser utilizada."
  }

  variable "api_ingress_path" {
    type        = string
    description = "Caminho de ingress da API."
    default = "/reports"
  }

  variable "ws_operation_id" {
    description = "Identificador único da operação WebSocket, utilizado para referência interna na definição do recurso"
    type        = string
    default     = "ws_reports"
  }

  variable "ws_display_name" {
    description = "Nome legível da operação WebSocket, exibido em portais, dashboards ou documentação"
    type        = string
    default     = "WebSocket Reports"
  }

  variable "ws_method" {
    description = "Método HTTP associado à operação WebSocket"
    type        = string
    default     = "GET"
  }

  variable "ws_url_template" {
    description = "Template do caminho da URL exposta pela operação WebSocket"
    type        = string
    default     = "/ws"
  }

# APIM
  variable "apim_api_name" {
    description = "Nome da API no API Management"
    type        = string
    default = "videocore-reports"
  }

  variable "apim_api_version" {
    description = "Versão da API no API Management"
    type        = string
    default     = "1"
  }

  variable "apim_display_name" {
    description = "Nome exibido da API no API Management"
    type        = string
    default     = "VideoCore Reports Microsservice API"
  }

  variable "apim_ws_api_name" {
    description = "Nome da API WebSocket no API Management"
    type        = string
    default     = "videocore-reports-ws"
  }

  variable "apim_ws_api_version" {
    description = "Versão da API WebSocket no API Management"
    type        = string
    default     = "1"
  }

  variable "apim_ws_display_name" {
    description = "Nome exibido da API WebSocket no API Management"
    type        = string
    default     = "VideoCore Reports Microsservice WebSocket API"
  }

  variable "swagger_path" {
    description = "Caminho do arquivo swagger.json"
    type        = string
  }