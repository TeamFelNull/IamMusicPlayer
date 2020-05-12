package net.morimori.imp.file;

public class FileReceiverBuffer {

	private int cont;
	public int allcont;
	private byte[] bytes;
	public String filepath;

	public FileReceiverBuffer(int bytecont, String path) {
		this.bytes = new byte[bytecont];
		this.allcont =bytecont;
		this.cont = 0;
		this.filepath = path;
	}

	public byte[] getBytes() {
		return this.bytes;
	}

	public int getCont() {
		return this.cont;
	}

	public void addBytes(byte[] addedbytes) {
		for (int i = 0; i < addedbytes.length; i++) {
			this.bytes[cont + i] = addedbytes[i];
		}
		this.cont += addedbytes.length;
	}

	public boolean isPerfectByte() {

		return cont == bytes.length;
	}
	public  String getPrograsePar() {

		return Math.round(((float) cont / (float) allcont) * 100) + " %";
	}
}
