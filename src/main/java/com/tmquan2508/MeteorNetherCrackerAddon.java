package com.tmquan2508;

import com.tmquan2508.commands.MeteorNetherCrackerCommand;
import com.mojang.logging.LogUtils;
import meteordevelopment.meteorclient.addons.GithubRepo;
import meteordevelopment.meteorclient.addons.MeteorAddon;
import meteordevelopment.meteorclient.commands.Commands;
import org.slf4j.Logger;

public class MeteorNetherCrackerAddon extends MeteorAddon {
    public static final Logger LOG = LogUtils.getLogger();

    @Override
    public void onInitialize() {
        LOG.info("Initializing Meteor Nether Cracker");
        Commands.add(new MeteorNetherCrackerCommand());
    }

    @Override
    public String getPackage() {
        return "com.tmquan2508";
    }

    @Override
    public GithubRepo getRepo() {
        return new GithubRepo("tmquan2508", "meteor-nether-cracker");
    }
}
