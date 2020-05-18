package net.morimori.imp.sound;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.UnsupportedTagException;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class SoundData {
	public String track = "null";
	public String artist = "null";
	public String album = "null";
	public String title = "null";
	public String year = "null";
	public String genre = "null";
	public String comment = "null";
	public String composet = "null";
	public String originalartist = "null";
	public String copyright = "null";
	public String url = "null";
	public String albumimage = "null";
	public String bitrate = "null";
	public String layer = "null";
	public String emphasis = "null";
	public byte[] album_image = null;

	public SoundData(Path path) {
		try {
			Mp3File mfile = new Mp3File(path.toString());
			ID3v2 tag = mfile.getId3v2Tag();

			if (tag == null) {
				return;
			}

			if (tag.getTrack() != null)
				this.track = tag.getTrack();

			if (tag.getArtist() != null)
				this.artist = tag.getArtist();

			if (tag.getAlbum() != null)
				this.album = tag.getAlbum();

			if (tag.getTitle() != null)
				this.title = tag.getTitle();

			if (tag.getYear() != null)
				this.year = tag.getYear();

			if (tag.getGenreDescription() != null)
				this.genre = tag.getGenreDescription();

			if (tag.getComment() != null)
				this.comment = tag.getComment();

			if (tag.getOriginalArtist() != null)
				this.originalartist = tag.getOriginalArtist();

			if (tag.getCopyright() != null)
				this.copyright = tag.getCopyright();

			if (tag.getUrl() != null)
				this.url = tag.getUrl();

			if (tag.getAlbum() != null)
				this.albumimage = tag.getAlbum();

			if (tag.getComposer() != null)
				this.composet = tag.getComposer();

			this.bitrate = String.valueOf(mfile.getBitrate());

			if (mfile.getLayer() != null)
				this.layer = mfile.getLayer();

			if (mfile.getEmphasis() != null)
				this.emphasis = mfile.getEmphasis();

			if (tag.getAlbumImage() != null)
				this.album_image = tag.getAlbumImage();

		} catch (UnsupportedTagException | InvalidDataException | IOException e) {

		}
	}

	public SoundData(CompoundNBT tag) {
		readNBT(tag);
	}

	public void readNBT(CompoundNBT tag) {

		if (tag == null) {
			return;
		}

		if (tag.contains("Track"))
			this.track = tag.getString("Track");

		if (tag.contains("Artist"))
			this.artist = tag.getString("Artist");

		if (tag.contains("Album"))
			this.album = tag.getString("Album");

		if (tag.contains("Title"))
			this.title = tag.getString("Title");

		if (tag.contains("Year"))
			this.year = tag.getString("Year");

		if (tag.contains("Genre"))
			this.genre = tag.getString("Genre");

		if (tag.contains("Comment"))
			this.comment = tag.getString("Comment");

		if (tag.contains("OriginalArtist"))
			this.originalartist = tag.getString("OriginalArtist");

		if (tag.contains("Copyright"))
			this.copyright = tag.getString("Copyright");

		if (tag.contains("Url"))
			this.url = tag.getString("Url");

		if (tag.contains("AlbumImage"))
			this.albumimage = tag.getString("AlbumImage");

		if (tag.contains("Composet"))
			this.composet = tag.getString("Composet");

		if (tag.contains("BitRate"))
			this.bitrate = tag.getString("BitRate");

		if (tag.contains("Layer"))
			this.layer = tag.getString("Layer");

		if (tag.contains("Emphasis"))
			this.emphasis = tag.getString("Emphasis");

		if (tag.contains("AlbumImage"))
			this.album_image = tag.getByteArray("AlbumImage");

	}

	public CompoundNBT writeNBT(CompoundNBT tag) {

		tag.putString("Track", this.track);
		tag.putString("Artist", this.artist);
		tag.putString("Album", this.album);
		tag.putString("Title", this.title);
		tag.putString("Year", this.year);
		tag.putString("Genre", this.genre);
		tag.putString("Comment", this.comment);
		tag.putString("OriginalArtist", this.originalartist);
		tag.putString("Copyright", this.copyright);
		tag.putString("Url", this.url);
		tag.putString("AlbumImage", this.albumimage);
		tag.putString("Composet", this.composet);
		tag.putString("BitRate", this.bitrate);
		tag.putString("Layer", this.layer);
		tag.putString("Emphasis", this.emphasis);

		if (this.album_image != null) {
			tag.putByteArray("AlbumImage", this.album_image);
		}

		return tag;
	}

	@OnlyIn(Dist.CLIENT)
	public static void addSoundDataTooltip(ItemStack itemIn, List<ITextComponent> lores) {

		SoundData sd = WorldPlayListSoundData.getWorldPlayListData(itemIn).getSoundData();

		if (sd == null)
			return;

		if (sd.title != null && !sd.title.equals("null") && !sd.title.isEmpty())
			lores.add(new TranslationTextComponent("soundata.title", sd.title));

		if (sd.artist != null && !sd.artist.equals("null") && !sd.artist.isEmpty())
			lores.add(new TranslationTextComponent("soundata.artist", sd.artist));

		Minecraft mc = Minecraft.getInstance();
		if (!mc.gameSettings.advancedItemTooltips) {
			return;
		}

		String album = null;

		if (sd.album != null && !sd.album.equals("null") && !sd.album.isEmpty()) {
			lores.add(new TranslationTextComponent("soundata.album", sd.album));
			album = sd.album;
		}

		if ((album == null || !album.equals(sd.albumimage)) && sd.albumimage != null && !sd.albumimage.equals("null")
				&& !sd.albumimage.isEmpty())
			lores.add(new TranslationTextComponent("soundata.albumimage", sd.albumimage));

		if (sd.year != null && !sd.year.equals("null") && !sd.year.isEmpty())
			lores.add(new TranslationTextComponent("soundata.year", sd.year));

		if (sd.track != null && !sd.track.equals("null") && !sd.track.isEmpty())
			lores.add(new TranslationTextComponent("soundata.track", sd.track));

		if (sd.genre != null && !sd.genre.equals("null") && !sd.genre.isEmpty())
			lores.add(new TranslationTextComponent("soundata.genre", sd.genre));

		if (sd.comment != null && !sd.comment.equals("null") && !sd.comment.isEmpty())
			lores.add(new TranslationTextComponent("soundata.comment", sd.comment));

		if (sd.composet != null && !sd.composet.equals("null") && !sd.composet.isEmpty())
			lores.add(new TranslationTextComponent("soundata.composet", sd.composet));

		if (sd.originalartist != null && !sd.originalartist.equals("null") && !sd.originalartist.isEmpty())
			lores.add(new TranslationTextComponent("soundata.originalartist", sd.originalartist));

		if (sd.copyright != null && !sd.copyright.equals("null") && !sd.copyright.isEmpty())
			lores.add(new TranslationTextComponent("soundata.copyright", sd.copyright));

		if (sd.url != null && !sd.url.equals("null") && !sd.url.isEmpty())
			lores.add(new TranslationTextComponent("soundata.url", sd.url));

		if (sd.bitrate != null && !sd.bitrate.equals("null") && !sd.bitrate.isEmpty())
			lores.add(new TranslationTextComponent("soundata.bitrate", sd.bitrate));

		if (sd.layer != null && !sd.layer.equals("null") && !sd.layer.isEmpty())
			lores.add(new TranslationTextComponent("soundata.layer", sd.layer));

		if (sd.emphasis != null && !sd.emphasis.equals("null") && !sd.emphasis.equals("None") && !sd.emphasis.isEmpty())
			lores.add(new TranslationTextComponent("soundata.emphasis", sd.emphasis));

	}

}
