package com.fabric.org;



public class PeerOrganisations {
		String id;
		String url;
		String tlsca;
		String peerMSP;
		String tlsEnabled;
		String peerType;
		String orgName;
		public String getPeerMSP() {
			return peerMSP;
		}
		public void setPeerMSP(String peerMSP) {
			this.peerMSP = peerMSP;
		}
		public String getOrgName() {
			return orgName;
		}
		public void setOrgName(String orgName) {
			this.orgName = orgName;
		}
		public CAOrganisation getCa() {
			return ca;
		}
		public void setCa(CAOrganisation ca) {
			this.ca = ca;
		}
		CAOrganisation ca;
		public String getPeerType() {
			return peerType;
		}
		public void setPeerType(String peerType) {
			this.peerType = peerType;
		}
		
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
			return peerMSP;
		}
		public void setOrdererMSP(String ordererMSP) {
			this.peerMSP = ordererMSP;
		}
		public String getTlsEnabled() {
			return tlsEnabled;
		}
		public void setTlsEnabled(String tlsEnabled) {
			this.tlsEnabled = tlsEnabled;
		}
		public CAOrganisation getCaOrganisation() {
			return ca;
		}
		public void setCaOrganisations(CAOrganisation caOrganisations) {
			this.ca = caOrganisations;
		}
		@Override
		public String toString() {
			return "PeerOrganisations [id=" + id + ", url=" + url + ", tlsca=" + tlsca + ", peerMSP=" + peerMSP
					+ ", tlsEnabled=" + tlsEnabled + ", peerType=" + peerType + ", orgName=" + orgName + ", ca=" + ca
					+ "]";
		}
		
	

}
