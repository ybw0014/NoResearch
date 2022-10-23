package net.guizhanss.noresearch.implementation;

import io.github.thebusybiscuit.slimefun4.libraries.dough.updater.GitHubBuildsUpdater;
import net.guizhanss.guizhanlib.localization.MinecraftLocalization;
import net.guizhanss.guizhanlib.slimefun.addon.AbstractAddon;
import net.guizhanss.guizhanlib.slimefun.addon.AddonConfig;
import net.guizhanss.guizhanlib.updater.GuizhanBuildsUpdater;
import org.bukkit.plugin.Plugin;

import javax.annotation.Nonnull;
import java.io.File;
import java.lang.reflect.Method;
import java.util.logging.Level;

public final class NoResearch extends AbstractAddon {

    private static final String DEFAULT_LANG = "en-US";

    // localization
    private MinecraftLocalization localization;

    public NoResearch() {
        super("ybw0014", "NoResearch", "master", "options.auto-update");
    }

    @Nonnull
    public static MinecraftLocalization getLocalization() {
        return ((NoResearch) getInstance()).localization;
    }

    @Override
    public void enable() {
        // config
        AddonConfig config = getAddonConfig();

        // Localization
        log(Level.INFO, "Loading language...");
        String lang = config.getString("options.lang", DEFAULT_LANG);
        localization = new MinecraftLocalization(this);
        localization.addLanguage(lang);
        if (!lang.equals(DEFAULT_LANG)) {
            localization.addLanguage(DEFAULT_LANG);
        }
        log(Level.INFO, "Loaded language {0}", lang);
    }

    @Override
    protected void disable() {
        // nothing yet
    }

    @Override
    protected void autoUpdate() {
        if (getDescription().getVersion().startsWith("Build")) {
            try {
                // use updater in lib plugin
                Class<?> clazz = Class.forName("net.guizhanss.guizhanlibplugin.updater.GuizhanBuildsUpdaterWrapper");
                Method updaterStart = clazz.getDeclaredMethod("start", Plugin.class, File.class, String.class, String.class, String.class, Boolean.class);
                updaterStart.invoke(null, this, getFile(), getGithubUser(), getGithubRepo(), getGithubBranch(), false);
            } catch (Exception ignored) {
                // use updater in lib
                new GuizhanBuildsUpdater(this, getFile(), getGithubUser(), getGithubRepo(),  getGithubBranch(), false).start();
            }
        } else if (getDescription().getVersion().startsWith("DEV")) {
            new GitHubBuildsUpdater(this, getFile(), getGithubUser() + "/" + getGithubRepo() + "/" + getGithubBranch()).start();
        }
    }

    @Override
    @Nonnull
    public String getBugTrackerURL() {
        return "https://github.com/ybw0014/NoResearch/issues";
    }
}
