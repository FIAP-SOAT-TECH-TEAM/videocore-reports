output "acr_login_server_from_remote" {
  value = data.azurerm_container_registry.acr.login_server
}

output "api_reports_private_dns_fqdn_from_remote" {
  value = data.terraform_remote_state.infra.outputs.api_reports_private_dns_fqdn
}