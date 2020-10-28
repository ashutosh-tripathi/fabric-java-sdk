package com.fabric.org;

import java.util.ArrayList;
import java.util.Arrays;

public class OrgChannel {
		String channelName;
		ArrayList<String> orgList;
		@Override
		public String toString() {
			return "Channel [channelName=" + channelName + ", orgList=" + orgList + "]";
		}

		public ArrayList<String> getOrgList() {
			return orgList;
		}

		public void setOrgList(ArrayList<String> orgList) {
			this.orgList = orgList;
		}

		public String getChannelName() {
			return channelName;
		}
		
		public void setChannelName(String channelName) {
			this.channelName = channelName;
		}
		
	
}
