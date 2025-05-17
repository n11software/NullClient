package net.optifine.player;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.net.URL;
import java.util.UUID;
import java.util.regex.Pattern;

import com.sun.net.ssl.HttpsURLConnection;

import n11client.utils.Log;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.SimpleTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.src.Config;
import net.minecraft.util.ResourceLocation;

public class CapeUtils
{
    private static final Pattern PATTERN_USERNAME = Pattern.compile("[a-zA-Z0-9_]+");

    public static void downloadCape(AbstractClientPlayer player)
    {
    	String s = player.getNameClear();

    	if (s != null && !s.isEmpty() && !s.contains("\u0000") && PATTERN_USERNAME.matcher(s).matches()) {
    	    String uuid = EntityPlayer.getUUID(player.getGameProfile()).toString();
    	    String s1 = "https://n11.dev/" + uuid + "/cape.png";
    	    Log.warn("Attempting to download cape from: " + s1);
    	    
    	    ResourceLocation resourcelocation = new ResourceLocation("capeof/" + s);
    	    TextureManager texturemanager = Minecraft.getMinecraft().getTextureManager();
    	    ITextureObject itextureobject = texturemanager.getTexture(resourcelocation);

    	    if (itextureobject instanceof ThreadDownloadImageData) {
    	        ThreadDownloadImageData existingData = (ThreadDownloadImageData) itextureobject;

    	        if (existingData.imageFound != null && existingData.imageFound) {
    	            player.setLocationOfCape(resourcelocation);

    	            if (existingData.getImageBuffer() instanceof CapeImageBuffer) {
    	                CapeImageBuffer capeBuffer = (CapeImageBuffer) existingData.getImageBuffer();
    	                player.setElytraOfCape(capeBuffer.isElytraOfCape());
    	            }
    	            return;
    	        }
    	    }

    	    // Run the download in a separate thread
    	    new Thread(() -> {
    	        try {
    	            // Verify HTTPS connection
    	            SSLUtil.disableSSLVerification();
    	            javax.net.ssl.HttpsURLConnection connection = 
    	                (javax.net.ssl.HttpsURLConnection) new URL(s1).openConnection();
    	            connection.setRequestMethod("GET");
    	            connection.setConnectTimeout(5000);
    	            connection.setReadTimeout(5000);

    	            int responseCode = connection.getResponseCode();
    	            if (responseCode == javax.net.ssl.HttpsURLConnection.HTTP_OK) {
    	                Log.warn("Successfully connected to " + s1);
    	                CapeImageBuffer capeimagebuffer = new CapeImageBuffer(player, resourcelocation);
    	                ThreadDownloadImageData imageData = 
    	                    new ThreadDownloadImageData(null, s1, null, capeimagebuffer);
    	                imageData.pipeline = true;
    	                texturemanager.loadTexture(resourcelocation, imageData);
    	            } else {
    	                Log.warn("Failed to download cape: Response code " + responseCode);
    	            }

    	            connection.disconnect();
    	        } catch (Exception e) {
    	            Log.error("Error downloading cape: " + e.getMessage());
    	            e.printStackTrace();
    	        }
    	    }).start();
    	}
    }

    public static BufferedImage parseCape(BufferedImage img)
    {
        int i = 64;
        int j = 32;
        int k = img.getWidth();

        for (int l = img.getHeight(); i < k || j < l; j *= 2)
        {
            i *= 2;
        }

        BufferedImage bufferedimage = new BufferedImage(i, j, 2);
        Graphics graphics = bufferedimage.getGraphics();
        graphics.drawImage(img, 0, 0, (ImageObserver)null);
        graphics.dispose();
        return bufferedimage;
    }

    public static boolean isElytraCape(BufferedImage imageRaw, BufferedImage imageFixed)
    {
        return imageRaw.getWidth() > imageFixed.getHeight();
    }

    public static void reloadCape(AbstractClientPlayer player)
    {
        String s = player.getNameClear();
        ResourceLocation resourcelocation = new ResourceLocation("capeof/" + s);
        TextureManager texturemanager = Config.getTextureManager();
        ITextureObject itextureobject = texturemanager.getTexture(resourcelocation);

        if (itextureobject instanceof SimpleTexture)
        {
            SimpleTexture simpletexture = (SimpleTexture)itextureobject;
            simpletexture.deleteGlTexture();
            texturemanager.deleteTexture(resourcelocation);
        }

        player.setLocationOfCape((ResourceLocation)null);
        player.setElytraOfCape(false);
        downloadCape(player);
    }
}
