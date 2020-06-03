package net.morimori.imp.client.screen;

import java.io.File;
import java.nio.file.Paths;

import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.gui.widget.button.ImageButton;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.morimori.imp.IkisugiMusicPlayer;
import net.morimori.imp.block.CassetteDeckBlock;
import net.morimori.imp.block.CassetteDeckStates;
import net.morimori.imp.block.IMPBlocks;
import net.morimori.imp.container.CassetteDeckContainer;
import net.morimori.imp.file.PlayList;
import net.morimori.imp.packet.CassetteDeckMessage;
import net.morimori.imp.packet.PacketHandler;
import net.morimori.imp.sound.WorldPlayListSoundData;
import net.morimori.imp.tileentity.CassetteDeckTileEntity;
import net.morimori.imp.util.PlayerHelper;
import net.morimori.imp.util.SoundHelper;
import net.morimori.imp.util.StringHelper;

public class CassetteDeckScreen extends ContainerScreen<CassetteDeckContainer> {
	protected static final ResourceLocation CD_GUI_TEXTURE = new ResourceLocation(IkisugiMusicPlayer.MODID,
			"textures/gui/container/cassette_deck.png");
	private static Minecraft mc = Minecraft.getInstance();
	private int page;
	private ServerFileSelectTarget serverSelectTargetR;

	private FaseStringImageButton fuleb1;
	private FaseStringImageButton fuleb2;
	private FaseStringImageButton fuleb3;
	private FaseStringImageButton fuleb4;
	private FaseStringImageButton fuleb5;
	private FaseStringImageButton fuleb6;
	private FaseStringImageButton fuleb7;

	private Button back;
	private Button next;
	private Button reload;
	private Button serverselecttarget;

	public static int sliselectfilestringL;
	public static int maxsliselectfilestringL;

	public CassetteDeckScreen(CassetteDeckContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
		super(screenContainer, inv, titleIn);
		serverSelectTargetR = ServerFileSelectTarget.MAIN;
		this.xSize = 231;
	}

	public String getSelectedName(String name) {

		String outst = name;

		maxsliselectfilestringL = name.length();

		int limitc = 45;

		if (getMaxPage(PlayList.worldPlayList) == 0 || getMaxPage(PlayList.worldPlayList) == 1) {
			limitc = 82;
		}

		if (this.font.getStringWidth(
				outst) > ((getMaxPage(PlayList.worldPlayList) == 0 || getMaxPage(PlayList.worldPlayList) == 1) ? 44 + 28
						: 33)) {
			outst = StringHelper.slipString(outst, true, sliselectfilestringL);
		}
		return StringHelper.characterLimit(mc, outst, limitc, false);
	}

	@Override
	public void init() {
		super.init();
		if (mc.world.getTileEntity(this.container.pos) instanceof CassetteDeckTileEntity) {
			CassetteDeckTileEntity sfit = (CassetteDeckTileEntity) mc.world
					.getTileEntity(this.container.pos);

			if (sfit.playerstager.containsKey(PlayerHelper.getUUID(mc.player))) {

				serverSelectTargetR = ServerFileSelectTarget
						.valueOf(sfit.playerstager.get(PlayerHelper.getUUID(mc.player)).toUpperCase());

			}
		}
		page = 1;
		serverFilelistUpdate();
		int xs = (this.width - this.xSize) / 2;
		int ys = (this.height - this.ySize) / 2;

		if (!(mc.world.getTileEntity(this.container.pos) instanceof CassetteDeckTileEntity))
			return;

		CassetteDeckTileEntity sfit = (CassetteDeckTileEntity) mc.world
				.getTileEntity(this.container.pos);

		this.addButton(new ImageButton(xs + 117, ys + 60, 22,
				17, 0, 166, 17, CD_GUI_TEXTURE, 256, 256, (p_213096_1_) -> {
					if (isAntena() && SoundHelper.canWriteCassette(getWriteCassette()) && !(sfit.getFliePath() == null
							|| sfit.getFliePath().isEmpty() || sfit.getFliePath().equals("null"))) {
						sendCDPacket(0);
					}
				}, I18n.format("narrator.recording")));
		this.addButton(new ImageButton(xs + 117 + 22, ys + 60, 22,
				17, 22, 166, 17, CD_GUI_TEXTURE, 256, 256, (p_213096_1_) -> {
					if (SoundHelper.canPlay(getWriteCassette())) {
						sendCDPacket(1);
					}
				}, I18n.format("narrator.playing")));
		this.addButton(new ImageButton(xs + 117 + 22 * 2, ys + 60, 22,
				17, 22 * 2, 166, 17, CD_GUI_TEXTURE, 256, 256, (p_213096_1_) -> {
					sendCDPacket(2);
				}, I18n.format("narrator.waiting")));
		this.addButton(new ImageButton(xs + 117 + 22 * 3, ys + 60, 22,
				17, 22 * 3, 166, 17, CD_GUI_TEXTURE, 256, 256, (p_213096_1_) -> {
					sendCDPacket(3);
				}, I18n.format("narrator.deliting")));
		this.addButton(new ImageButton(xs + 117 + 22 * 4, ys + 60, 22,
				17, 22 * 4, 166, 17, CD_GUI_TEXTURE, 256, 256, (p_213096_1_) -> {
					if (SoundHelper.canWriteCassette(getWriteCassette())
							&& SoundHelper.isWritedSound(sfit.getCopyingCassete())
							&& !WorldPlayListSoundData.getWorldPlayListData(getWriteCassette())
									.equals(WorldPlayListSoundData.getWorldPlayListData(sfit.getCopyingCassete()))) {
						sendCDPacket(4);
					}
				}, I18n.format("narrator.copying")));

		fristSoundSelecter();

	}

	public void drawPrograse() {
		int xs = (this.width - this.xSize) / 2;
		int ys = (this.height - this.ySize) / 2;

		if (!(mc.world.getTileEntity(this.container.pos) instanceof CassetteDeckTileEntity))
			return;

		CassetteDeckTileEntity sfit = (CassetteDeckTileEntity) mc.world
				.getTileEntity(this.container.pos);

		RenderSystem.pushMatrix();
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.minecraft.getTextureManager().bindTexture(CD_GUI_TEXTURE);
		int Rprograsse = (int) (23 * ((float) sfit.recodingPrograse / (float) sfit.getRecodingTotalTime()));
		this.blit(xs + 120, ys + 38, 110, 166, Rprograsse, 16);

		this.minecraft.getTextureManager().bindTexture(CD_GUI_TEXTURE);
		int Cprograsse = (int) (23 * ((float) sfit.copyingPrograse / (float) sfit.getCoppyTotalTime()));
		this.blit(xs + 173 + 23 - Cprograsse, ys + 38, 110 + 23 - Cprograsse, 182, Cprograsse, 16);

		this.minecraft.getTextureManager().bindTexture(CD_GUI_TEXTURE);
		int Dprograsse = (int) (23 * ((float) sfit.deletingPrograse / (float) sfit.getDeleteTotalTime()));
		this.blit(xs + 120, ys + 38, 110, 166, Dprograsse, 16);
		this.minecraft.getTextureManager().bindTexture(CD_GUI_TEXTURE);
		this.blit(xs + 173 + 23 - Dprograsse, ys + 38, 110 + 23 - Dprograsse, 182, Dprograsse, 16);

		RenderSystem.popMatrix();

	}

	private boolean isAntena() {

		if (!(mc.world.getTileEntity(this.container.pos) instanceof CassetteDeckTileEntity))
			return false;

		CassetteDeckTileEntity sfit = (CassetteDeckTileEntity) mc.world
				.getTileEntity(this.container.pos);

		return !sfit.getAntenna().isEmpty();
	}

	private void drawFilelistUpdate() {

		int coloer = 16729344;

		int xs = (this.width - this.xSize) / 2;
		int ys = (this.height - this.ySize) / 2;
		if (isAntena() && hasOn()) {
			RenderSystem.pushMatrix();
			RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
			this.minecraft.getTextureManager().bindTexture(CD_GUI_TEXTURE);
			this.blit(xs + 44 + 8, ys + 64, 44, 216, 63, 12);
			RenderSystem.popMatrix();
			RenderSystem.pushMatrix();
			RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
			this.minecraft.getTextureManager().bindTexture(CD_GUI_TEXTURE);
			this.blit(xs + 8, ys + 64, 44, 216, 63, 12);
			RenderSystem.popMatrix();

			if (PlayList.worldPlayList != null) {
				if (!(getMaxPage(PlayList.worldPlayList) == 0 || getMaxPage(PlayList.worldPlayList) == 1)) {
					RenderSystem.pushMatrix();
					this.font.drawString(this.page + "/" + this.getMaxPage(PlayList.worldPlayList), xs + 44 + 10,
							ys + 66, coloer);
					RenderSystem.popMatrix();
				}
			}
			if (mc.world.getTileEntity(this.container.pos) instanceof CassetteDeckTileEntity) {
				CassetteDeckTileEntity sfit = (CassetteDeckTileEntity) mc.world
						.getTileEntity(this.container.pos);
				if (sfit.getFliePath() != null && !sfit.getFliePath().isEmpty() && !sfit.getFliePath().equals("null")) {
					int x = 44 + 28;
					if (getMaxPage(PlayList.worldPlayList) == 0 || getMaxPage(PlayList.worldPlayList) == 1) {
						x = 33;
					}

					drawString(getSelectedName(
							I18n.format("cd.selected", Paths.get(sfit.getFliePath()).toFile().getName())),
							x, 66, coloer);
				}
			}
		} else {

			if (hasOn()) {
				drawString(I18n.format("sfu.selectfileupdate.noantena"), 9, 9, coloer);
			}
		}

	}

	private void serverFilelistUpdate() {
		page = 1;
		if (serverSelectTargetR == ServerFileSelectTarget.MAIN) {
			sendCDPacket(5);
		} else if (serverSelectTargetR == ServerFileSelectTarget.EVERYONE) {
			sendCDPacket(6);
		}
	}

	private String playerNameGetter(int contb) {

		if (PlayList.worldPlayListNames.length == 0) {
			return null;
		}
		if (getMaxPage(PlayList.worldPlayList) == 0) {
			return PlayList.worldPlayListNames[contb - 1];
		}

		return PlayList.worldPlayListNames[7 * (page - 1) + contb - 1];
	}

	private String fileNameGetter(int contb, File[] afilses) {
		if (fileGetter(contb, afilses) == null)
			return "Error";

		return StringHelper.characterLimit(mc, fileGetter(contb, afilses).getName(), 99);

	}

	private File fileGetter(int contb, File[] afilses) {

		if (afilses.length == 0) {
			return null;
		}
		if (getMaxPage(afilses) == 0) {
			return afilses[contb - 1];
		}

		return afilses[7 * (page - 1) + contb - 1];
	}

	public void updateSoundSelecter() {

		if (isAntena() && hasOn()) {
			if (PlayList.worldPlayList != null) {
				int filecont = PlayList.worldPlayList.length;
				int nowpagecont = page != getMaxPage(PlayList.worldPlayList) ? 7
						: 7 - (getMaxPage(PlayList.worldPlayList) * 7 - filecont);

				if (page == 1) {
					nowpagecont = filecont;
					if (filecont == 0) {
						nowpagecont = 0;
					}
				}

				if (nowpagecont >= 1) {
					fuleb1.visible = true;
					fuleb1.setMessage(fileNameGetter(1, PlayList.worldPlayList));
					if (serverSelectTargetR == ServerFileSelectTarget.EVERYONE) {
						fuleb1.isDrawPlayerFase = true;
						fuleb1.setFasResourceLocation(playerNameGetter(1));
					} else {
						fuleb1.isDrawPlayerFase = false;
					}

				} else {
					fuleb1.visible = false;
				}
				if (nowpagecont >= 2) {
					fuleb2.visible = true;
					fuleb2.setMessage(fileNameGetter(2, PlayList.worldPlayList));
					if (serverSelectTargetR == ServerFileSelectTarget.EVERYONE) {
						fuleb2.isDrawPlayerFase = true;
						fuleb2.setFasResourceLocation(playerNameGetter(2));
					} else {
						fuleb2.isDrawPlayerFase = false;
					}
				} else {
					fuleb2.visible = false;
				}
				if (nowpagecont >= 3) {
					fuleb3.visible = true;
					fuleb3.setMessage(fileNameGetter(3, PlayList.worldPlayList));
					if (serverSelectTargetR == ServerFileSelectTarget.EVERYONE) {
						fuleb3.isDrawPlayerFase = true;
						fuleb3.setFasResourceLocation(playerNameGetter(3));
					} else {
						fuleb3.isDrawPlayerFase = false;
					}
				} else {
					fuleb3.visible = false;
				}
				if (nowpagecont >= 4) {
					fuleb4.visible = true;
					fuleb4.setMessage(fileNameGetter(4, PlayList.worldPlayList));
					if (serverSelectTargetR == ServerFileSelectTarget.EVERYONE) {
						fuleb4.isDrawPlayerFase = true;
						fuleb4.setFasResourceLocation(playerNameGetter(4));
					} else {
						fuleb4.isDrawPlayerFase = false;
					}
				} else {
					fuleb4.visible = false;
				}
				if (nowpagecont >= 5) {
					fuleb5.visible = true;
					fuleb5.setMessage(fileNameGetter(5, PlayList.worldPlayList));
					if (serverSelectTargetR == ServerFileSelectTarget.EVERYONE) {
						fuleb5.isDrawPlayerFase = true;
						fuleb5.setFasResourceLocation(playerNameGetter(5));
					} else {
						fuleb5.isDrawPlayerFase = false;
					}
				} else {
					fuleb5.visible = false;
				}
				if (nowpagecont >= 6) {
					fuleb6.visible = true;
					fuleb6.setMessage(fileNameGetter(6, PlayList.worldPlayList));
					if (serverSelectTargetR == ServerFileSelectTarget.EVERYONE) {
						fuleb6.isDrawPlayerFase = true;
						fuleb6.setFasResourceLocation(playerNameGetter(6));
					} else {
						fuleb6.isDrawPlayerFase = false;
					}
				} else {
					fuleb6.visible = false;
				}
				if (nowpagecont >= 7) {
					fuleb7.visible = true;
					fuleb7.setMessage(fileNameGetter(7, PlayList.worldPlayList));
					if (serverSelectTargetR == ServerFileSelectTarget.EVERYONE) {
						fuleb7.isDrawPlayerFase = true;
						fuleb7.setFasResourceLocation(playerNameGetter(7));
					} else {
						fuleb7.isDrawPlayerFase = false;
					}
				} else {
					fuleb7.visible = false;
				}
				if (!(getMaxPage(PlayList.worldPlayList) == 0 || getMaxPage(PlayList.worldPlayList) == 1)) {
					back.visible = true;
					next.visible = true;
				} else {
					back.visible = false;
					next.visible = false;

				}
				reload.visible = true;
				serverselecttarget.visible = true;

				if (serverSelectTargetR == ServerFileSelectTarget.MAIN) {
					((FaseStringImageButton) serverselecttarget).isDrawPlayerFase = true;
				} else {
					((FaseStringImageButton) serverselecttarget).isDrawPlayerFase = false;
				}
			}
		} else {

			fuleb1.visible = false;
			fuleb2.visible = false;
			fuleb3.visible = false;
			fuleb4.visible = false;
			fuleb5.visible = false;
			fuleb6.visible = false;
			fuleb7.visible = false;
			back.visible = false;
			next.visible = false;
			reload.visible = false;
			serverselecttarget.visible = false;

		}
	}

	public void selectSound(int num) {
		sliselectfilestringL = 0;
		sendCDPacket(7, fileGetter(num, PlayList.worldPlayList).getPath().toString());
	}

	private int getMaxPage(File[] afilse) {
		if (afilse != null) {
			return (int) Math.ceil((float) afilse.length / 7f);
		} else {
			return 0;
		}
	}

	public void fristSoundSelecter() {
		int xs = (this.width - this.xSize) / 2;
		int ys = (this.height - this.ySize) / 2;
		int coloer = 16729344;

		fuleb1 = new FaseStringImageButton(xs + 8, ys + 8, 107,
				8, 0, 200, 8, CD_GUI_TEXTURE, 256, 256, (p_213096_1_) -> {
					selectSound(1);
				}, "", -44, false);
		fuleb2 = new FaseStringImageButton(xs + 8, ys + 8 + 8 * 1, 107,
				8, 0, 200, 8, CD_GUI_TEXTURE, 256, 256, (p_213096_1_) -> {
					selectSound(2);
				}, "", -44, false);
		fuleb3 = new FaseStringImageButton(xs + 8, ys + 8 + 8 * 2, 107,
				8, 0, 200, 8, CD_GUI_TEXTURE, 256, 256, (p_213096_1_) -> {
					selectSound(3);
				}, "", -44, false);
		fuleb4 = new FaseStringImageButton(xs + 8, ys + 8 + 8 * 3, 107,
				8, 0, 200, 8, CD_GUI_TEXTURE, 256, 256, (p_213096_1_) -> {
					selectSound(4);
				}, "", -44, false);
		fuleb5 = new FaseStringImageButton(xs + 8, ys + 8 + 8 * 4, 107,
				8, 0, 200, 8, CD_GUI_TEXTURE, 256, 256, (p_213096_1_) -> {
					selectSound(5);
				}, "", -44, false);
		fuleb6 = new FaseStringImageButton(xs + 8, ys + 8 + 8 * 5, 107,
				8, 0, 200, 8, CD_GUI_TEXTURE, 256, 256, (p_213096_1_) -> {
					selectSound(6);
				}, "", -44, false);
		fuleb7 = new FaseStringImageButton(xs + 8, ys + 8 + 8 * 6, 107,
				8, 0, 200, 8, CD_GUI_TEXTURE, 256, 256, (p_213096_1_) -> {
					selectSound(7);
				}, "", -44, false);

		fuleb1.visible = false;
		fuleb2.visible = false;
		fuleb3.visible = false;
		fuleb4.visible = false;
		fuleb5.visible = false;
		fuleb6.visible = false;
		fuleb7.visible = false;

		fuleb1.isReddish = true;
		fuleb2.isReddish = true;
		fuleb3.isReddish = true;
		fuleb4.isReddish = true;
		fuleb5.isReddish = true;
		fuleb6.isReddish = true;
		fuleb7.isReddish = true;

		fuleb1.setStringColoer(coloer);
		fuleb2.setStringColoer(coloer);
		fuleb3.setStringColoer(coloer);
		fuleb4.setStringColoer(coloer);
		fuleb5.setStringColoer(coloer);
		fuleb6.setStringColoer(coloer);
		fuleb7.setStringColoer(coloer);

		fuleb1.setFileSelectButton(true);
		fuleb2.setFileSelectButton(true);
		fuleb3.setFileSelectButton(true);
		fuleb4.setFileSelectButton(true);
		fuleb5.setFileSelectButton(true);
		fuleb6.setFileSelectButton(true);
		fuleb7.setFileSelectButton(true);

		this.addButton(fuleb1);
		this.addButton(fuleb2);
		this.addButton(fuleb3);
		this.addButton(fuleb4);
		this.addButton(fuleb5);
		this.addButton(fuleb6);
		this.addButton(fuleb7);

		back = new ImageButton(xs + 32, ys + 64, 10,
				12, 0, 216, 12, CD_GUI_TEXTURE, 256, 256, (p_213096_1_) -> {

					if (page > 1)
						page--;
					else
						page = 1;

				});

		this.addButton(back);
		back.visible = false;
		next = new ImageButton(xs + 42, ys + 64, 10,
				12, 10, 216, 12, CD_GUI_TEXTURE, 256, 256, (p_213096_1_) -> {

					if (page < getMaxPage(PlayList.worldPlayList))
						page++;
					else
						page = getMaxPage(PlayList.worldPlayList);

				});

		this.addButton(next);
		next.visible = false;

		reload = new ImageButton(xs + 20, ys + 64, 12,
				12, 20, 216, 12, CD_GUI_TEXTURE, 256, 256, (p_213096_1_) -> {
					serverFilelistUpdate();
				}, I18n.format("sfu.reload"));
		this.addButton(reload);
		reload.visible = false;

		serverselecttarget = new FaseStringImageButton(xs + 8, ys + 64,
				12, 12, 32, 216, 12,
				CD_GUI_TEXTURE, 256, 256, (p_213096_1_) -> {

					if (serverSelectTargetR == ServerFileSelectTarget.MAIN) {
						serverSelectTargetR = ServerFileSelectTarget.EVERYONE;
					} else if (serverSelectTargetR == ServerFileSelectTarget.EVERYONE) {
						serverSelectTargetR = ServerFileSelectTarget.MAIN;
					}
					sendCDPacket(8, serverSelectTargetR.toString());
					serverFilelistUpdate();
				}, "", false);

		this.addButton(serverselecttarget);
		serverselecttarget.visible = false;
		((FaseStringImageButton) serverselecttarget).isReddish = true;
	}

	public void drawGrennBack() {
		if (!hasOn())
			return;

		if (!getWriteCassette().isEmpty()) {
			drawString(StringHelper.characterLimit(mc, SoundHelper.getCassetteSoundName(getWriteCassette()), 106), 120,
					7, 2722312);
		}

		if (hasState(CassetteDeckStates.RECORD)) {
			drawString(I18n.format("cd.recording"), 120, 17, 2722312);
		} else if (hasState(CassetteDeckStates.PLAY)) {
			drawString(I18n.format("cd.playing"), 120, 17, 2722312);
		} else if (hasState(CassetteDeckStates.DELETE)) {
			drawString(I18n.format("cd.deliting"), 120, 17, 2722312);
		} else if (hasState(CassetteDeckStates.COPY)) {
			drawString(I18n.format("cd.copying"), 120, 17, 2722312);
		} else {
			drawString(I18n.format("cd.waiting"), 120, 17, 2722312);
		}

	}

	private boolean hasOn() {
		if (mc.world.getBlockState(this.container.pos).getBlock() != IMPBlocks.CASSETTE_DECK) {
			return false;
		}

		return mc.world.getBlockState(this.container.pos).get(CassetteDeckBlock.ON);
	}

	private boolean hasState(CassetteDeckStates cds) {

		if (mc.world.getBlockState(this.container.pos).getBlock() != IMPBlocks.CASSETTE_DECK) {
			return false;
		}

		return mc.world.getBlockState(this.container.pos)
				.get(CassetteDeckBlock.CASSETTE_DECK_STATES) == cds;
	}

	private ItemStack getWriteCassette() {
		if (!(mc.world.getTileEntity(this.container.pos) instanceof CassetteDeckTileEntity))
			return ItemStack.EMPTY;

		return ((CassetteDeckTileEntity) mc.world.getTileEntity(this.container.pos)).getWriteCassette();
	}

	private void drawString(String st, int x, int y, int col) {
		int xs = (this.width - this.xSize) / 2;
		int ys = (this.height - this.ySize) / 2;
		RenderSystem.pushMatrix();
		this.font.drawString(st, xs + x, ys + y, col);
		RenderSystem.popMatrix();
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		RenderSystem.pushMatrix();
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.minecraft.getTextureManager().bindTexture(CD_GUI_TEXTURE);
		int xs = (this.width - this.xSize) / 2;
		int ys = (this.height - this.ySize) / 2;
		this.blit(xs, ys, 0, 0, this.xSize, this.ySize);
		RenderSystem.popMatrix();
		drawGrennBack();
		drawFilelistUpdate();
		drawPrograse();
	}

	@Override
	public void tick() {
		super.tick();
		updateSoundSelecter();
	}

	@Override
	public boolean mouseScrolled(double p_mouseScrolled_1_, double p_mouseScrolled_3_, double p_mouseScrolled_5_) {

		if (!(getMaxPage(PlayList.worldPlayList) == 0 || getMaxPage(PlayList.worldPlayList) == 1)) {
			this.page = page - (int) p_mouseScrolled_5_;
			this.page = MathHelper.clamp(page, 1, getMaxPage(PlayList.worldPlayList));
		}

		return false;

	}

	@Override
	public void render(int p_render_1_, int p_render_2_, float p_render_3_) {
		this.renderBackground();
		super.render(p_render_1_, p_render_2_, p_render_3_);
		this.renderHoveredToolTip(p_render_1_, p_render_2_);

	}

	private void sendCDPacket(int num) {
		PacketHandler.INSTANCE.sendToServer(new CassetteDeckMessage(
				mc.world.getDimension().getType().getId(), this.container.pos, num));
	}

	private void sendCDPacket(int num, String st) {
		PacketHandler.INSTANCE.sendToServer(new CassetteDeckMessage(
				mc.world.getDimension().getType().getId(), this.container.pos, num, st));
	}
}
