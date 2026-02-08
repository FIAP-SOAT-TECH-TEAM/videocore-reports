# Commn
  variable "subscription_id" {
    type        = string
    description = "Azure Subscription ID"
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
    default = "default"
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

  variable "api_websocket_base_endpoint" {
    type        = string
    description = "Endpoint base para conexões WebSocket da API."
    default = "/ws"
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
    default     = "VideoCore Reports Microsservice"
  }

  variable "swagger_path" {
    description = "Caminho do arquivo swagger.json"
    type        = string
  }