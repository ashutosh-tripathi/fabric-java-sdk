package com.fabric.org;

public class OrdererOrganisations {
		String id;
		String url;
		String tlsca;
		String ordererMSP;
		String tlsEnabled;
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public String getUrl() {
			return url;
		}
		public void setUrl(String url) {
			this.url = url;
		}
		public String getTlsca() {
			return tlsca;
		}
		public void setTlsca(String tlsca) {
			this.tlsca = tlsca;
		}
		public String getOrdererMSP() {
			return ordererMSP;
		}
		public void setOrdererMSP(String ordererMSP) {
			this.ordererMSP = ordererMSP;
		}
		public String getTlsEnabled() {
			return tlsEnabled;
		}
		public void setTlsEnabled(String tlsEnabled) {
			this.tlsEnabled = tlsEnabled;
		}
		@Override
		public String toString() {
			return "OrdererOrganisations [id=" + id + ", url=" + url + ", tlsca=" + tlsca + ", ordererMSP=" + ordererMSP
					+ ", tlsEnabled=" + tlsEnabled + "]";
		}
		
	

}
