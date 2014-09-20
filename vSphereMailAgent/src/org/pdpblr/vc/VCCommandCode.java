package org.pdpblr.vc;

public enum VCCommandCode {
	VC_CMD_USERNAME(1),
	VC_CMD_PASSWORD(2),
	VC_CMD_VCIP(3),
	VC_CMD_GETVMNAMES(4),
	VC_CMD_GETHOSTS(5),
	VC_CMD_POWERON_VM(6),
	VC_CMD_POWEROFF_VM(7),
	VC_CMD_RESET_VM(8),
	VC_CMD_SUSPEND_VM(9),
	VC_CMD_REBOOT_VM(10),
	VC_CMD_RESUME_VM(11),
	VC_CMD_GETDATACENTERNAMES(12),
	VC_CMD_GETRESOURCEPOOL(13),
	VC_CMD_GETLOGFILE(14),
	VC_CMD_HELP(15),
	VC_CMD_HOSTVMOTION(16),
	VC_CMD_STORAGEVMOTION(17);
	
	private int code;
	VCCommandCode(int codeValue) {
		code = codeValue;
	}
	
	public static VCCommandCode getCommand(String command) {
		VCCommandCode[] commands = VCCommandCode.values();
		for (VCCommandCode cmd : commands) {
			if (command.equalsIgnoreCase(cmd.name()))
					return cmd;
		}
		return null;
	}
	
	public int getCode() {
		return code;
	}
}
