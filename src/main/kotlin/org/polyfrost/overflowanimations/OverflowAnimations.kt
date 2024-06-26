package org.polyfrost.overflowanimations

import cc.polyfrost.oneconfig.events.EventManager
import cc.polyfrost.oneconfig.events.event.RenderEvent
import cc.polyfrost.oneconfig.events.event.Stage
import cc.polyfrost.oneconfig.libs.eventbus.Subscribe
import cc.polyfrost.oneconfig.libs.universal.UDesktop
import cc.polyfrost.oneconfig.utils.Notifications
import cc.polyfrost.oneconfig.utils.commands.CommandManager
import cc.polyfrost.oneconfig.utils.gui.GuiUtils
import dulkirmod.config.Config
import dulkirmod.config.DulkirConfig
import net.minecraft.client.Minecraft
import net.minecraftforge.fml.common.Loader
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.event.FMLLoadCompleteEvent
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent
import org.polyfrost.overflowanimations.command.OldAnimationsCommand
import org.polyfrost.overflowanimations.config.OldAnimationsSettings
import org.polyfrost.overflowanimations.gui.PleaseMigrateDulkirModGui
import org.polyfrost.overflowanimations.init.CustomModelBakery
import java.net.URI

@Mod(
    modid = OverflowAnimations.MODID,
    name = OverflowAnimations.NAME,
    version = OverflowAnimations.VERSION,
    modLanguageAdapter = "cc.polyfrost.oneconfig.utils.KotlinLanguageAdapter"
)
object OverflowAnimations {

    const val MODID: String = "@ID@"
    const val NAME: String = "@NAME@"
    const val VERSION: String = "@VER@"

    @JvmField
    var isPatcherPresent: Boolean = false
    private var doTheFunnyDulkirThing = false
    @JvmField
    var oldDulkirMod: Boolean = false
    private var customCrosshair = false
    @JvmField
    var isDamageTintPresent: Boolean = false

    @Mod.EventHandler
    fun preInit(event: FMLPreInitializationEvent) {
        CustomModelBakery
    }

    @Mod.EventHandler
    fun init(event: FMLInitializationEvent) {
        OldAnimationsSettings.INSTANCE.preload()
        CommandManager.INSTANCE.registerCommand(OldAnimationsCommand())
        EventManager.INSTANCE.register(this)
    }

    @Mod.EventHandler
    fun postInit(event: FMLPostInitializationEvent) {
        if (Loader.isModLoaded("dulkirmod")) {
            doTheFunnyDulkirThing = true
        }
        isPatcherPresent = Loader.isModLoaded("patcher")
        customCrosshair = Loader.isModLoaded("custom-crosshair-mod")
        isDamageTintPresent = Loader.isModLoaded("damagetint")
    }

    @Mod.EventHandler
    fun onLoad(event: FMLLoadCompleteEvent) {
        if (customCrosshair) {
            OldAnimationsSettings.smoothModelSneak = false
            OldAnimationsSettings.INSTANCE.save()
            Notifications.INSTANCE.send("OverflowAnimations", "Custom Crosshair Mod has been detected, which is written poorly and causes major issues with OverflowAnimations. Disabling Smooth Model Sneak. If you want a better crosshair mod, please click here to use PolyCrosshair instead.", 5000f, Runnable {
                UDesktop.browse(URI("https://modrinth.com/mod/crosshair"))
            })
        }
    }

    @Subscribe
    private fun onTick(event: RenderEvent) {
        if (event.stage == Stage.START && Minecraft.getMinecraft().currentScreen == null && Minecraft.getMinecraft().theWorld != null && Minecraft.getMinecraft().thePlayer != null && doTheFunnyDulkirThing && !OldAnimationsSettings.didTheFunnyDulkirThingElectricBoogaloo) {
            try {
                Class.forName("dulkirmod.config.DulkirConfig")
                if (DulkirConfig.INSTANCE.customAnimations) {
                    dulkirTrollage()
                }
            } catch (e: ClassNotFoundException) {
                oldDulkirMod = true
                if (Config.INSTANCE.customAnimations) {
                    dulkirTrollage()
                }
            }
        }
    }

    private fun dulkirTrollage() {
        GuiUtils.displayScreen(PleaseMigrateDulkirModGui())
        doTheFunnyDulkirThing = false
        OldAnimationsSettings.didTheFunnyDulkirThingElectricBoogaloo = true
        OldAnimationsSettings.INSTANCE.save()
    }

}