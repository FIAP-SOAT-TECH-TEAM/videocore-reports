# Common
  variable "api_reports_private_dns_fqdn" {
    type = string
    description = "FQDN do registro A do microsserviço de reports na zona DNS privada"
  }
  
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

variable "apim_ws_api_name" {
  description = "Nome da API WebSocket no API Management"
  type        = string
}

variable "apim_ws_api_version" {
  description = "Versão da API WebSocket no API Management"
  type        = string
}

variable "apim_ws_display_name" {
  description = "Nome exibido da API WebSocket no API Management"
  type        = string
}
