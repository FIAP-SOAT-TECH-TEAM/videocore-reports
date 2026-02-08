# remote states
  variable "subscription_id" {
    type        = string
    description = "Azure Subscription ID"
  }
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

variable "apim_api_name" {
  description = "Nome da API no API Management"
  type        = string
}

variable "apim_api_version" {
  description = "Versão da API no API Management"
  type        = string
}

variable "apim_display_name" {
  description = "Nome exibido da API no API Management"
  type        = string
}

variable "swagger_path" {
  description = "Caminho do arquivo swagger.json"
  type        = string
}

variable "api_ingress_path" {
  type        = string
  description = "Caminho de ingress da API."
}

variable "ws_operation_id" {
  description = "Identificador único da operação WebSocket, utilizado para referência interna na definição do recurso"
  type        = string
}

variable "ws_display_name" {
  description = "Nome legível da operação WebSocket, exibido em portais, dashboards ou documentação"
  type        = string
}

variable "ws_method" {
  description = "Método HTTP associado à operação WebSocket"
  type        = string
}

variable "ws_url_template" {
  description = "Template do caminho da URL exposta pela operação WebSocket"
  type        = string
}