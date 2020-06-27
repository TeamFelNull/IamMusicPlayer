package red.felnull.imp.client.screen;

import net.minecraft.util.IStringSerializable;

public enum ServerFileSelectTarget implements IStringSerializable {
	MAIN("main"), EVERYONE("everyone");

	private final String name;

	ServerFileSelectTarget(String string) {
		this.name = string;
	}

	@Override
	public String func_176610_l() {

		return this.name;
	}

	public String toString() {
		return this.name;
	}

}
