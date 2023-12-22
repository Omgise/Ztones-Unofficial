package oneeyemaker.ztones.proxy;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.common.MinecraftForge;

import org.lwjgl.input.Keyboard;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import oneeyemaker.ztones.ModConfiguration;
import oneeyemaker.ztones.Tags;
import oneeyemaker.ztones.items.ZtoneGenericItemBlock;
import oneeyemaker.ztones.network.ModNetwork;

@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy {

    private static KeyBinding cycleKey;

    @Override
    public void initialize(FMLInitializationEvent event) {
        super.initialize(event);
        cycleKey = new KeyBinding(
            StatCollector.translateToLocal(String.format("%s.keybinding.cycle", Tags.MODID)),
            Keyboard.KEY_LCONTROL,
            StatCollector.translateToLocal(String.format("%s.keybindings", Tags.MODID)));
        ClientRegistry.registerKeyBinding(cycleKey);
        if (ModConfiguration.isVariantCyclingEnabled) {
            MinecraftForge.EVENT_BUS.register(this);
        }
    }

    @Override
    public World getClientWorld() {
        return Minecraft.getMinecraft().theWorld;
    }

    @SubscribeEvent
    public void processMouseEvent(MouseEvent event) {
        if (event.dwheel == 0 || !cycleKey.getIsKeyPressed()) {
            return;
        }
        Minecraft minecraft = Minecraft.getMinecraft();
        EntityPlayer entityPlayer = minecraft.thePlayer;
        if (entityPlayer != null && minecraft.currentScreen == null) {
            ItemStack itemStack = entityPlayer.getHeldItem();
            if (itemStack != null && itemStack.getItem() instanceof ZtoneGenericItemBlock) {
                ModNetwork.cycleZtone(entityPlayer, event.dwheel > 0);
                event.setCanceled(true);
            }
        }
    }
}
