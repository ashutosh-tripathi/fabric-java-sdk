package com.fabric.org;

public class CAOrganisation {
		String id;
		String url;
		String tlsca;
		String name;
		String enrollmentId;
		String enrollmentSecret;
		String orgName;
		public String getOrgName() {
			return orgName;
		}
		public void setOrgName(String orgName) {
			this.orgName = orgName;
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
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getEnrollmentId() {
			return enrollmentId;
		}
		public void setEnrollmentId(String enrollmentId) {
			this.enrollmentId = enrollmentId;
		}
		public String getEnrollmentSecret() {
			return enrollmentSecret;
		}
		public void setEnrollmentSecret(String enrollmentSecret) {
			this.enrollmentSecret = enrollmentSecret;
		}
		@Override
		public String toString() {
			return "CAOrganisation [id=" + id + ", url=" + url + ", tlsca=" + tlsca + ", name=" + name
					+ ", enrollmentId=" + enrollmentId + ", enrollmentSecret=" + enrollmentSecret + ", orgName="
					+ orgName + "]";
		}
		
	
}
