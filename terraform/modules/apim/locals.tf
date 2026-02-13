locals {
  reports_ws_service_url = "ws://${data.terraform_remote_state.infra.outputs.api_reports_private_dns_fqdn}/${var.api_ingress_path}"
}