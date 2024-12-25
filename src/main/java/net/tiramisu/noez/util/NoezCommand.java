package net.tiramisu.noez.util;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.GameType;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Collection;

@Mod.EventBusSubscriber
public class NoezCommand {
    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();

        dispatcher.register(Commands.literal("gmc")
                .requires(source -> source.hasPermission(2))
                .executes(NoezCommand::setCreativeMode));

        dispatcher.register(Commands.literal("gms")
                .requires(source -> source.hasPermission(2))
                .executes(NoezCommand::setSurvivalMode));

        dispatcher.register(Commands.literal("heal")
                .then(Commands.argument("entities", EntityArgument.entities())
                        .then(Commands.argument("amount", IntegerArgumentType.integer(0))
                                .executes(context -> healEntities(context, EntityArgument.getEntities(context, "entities"), IntegerArgumentType.getInteger(context, "amount")))))
                .executes(context -> {
                    context.getSource().sendFailure(Component.literal("Usage: /heal <amount> or /heal <entities> <amount>"));
                    return 0;
                }));

        dispatcher.register(Commands.literal("heals")
                .then(Commands.argument("amount", IntegerArgumentType.integer(0))
                        .executes(context -> healSelf(context, IntegerArgumentType.getInteger(context, "amount"))))
                .executes(context -> {
                    context.getSource().sendFailure(Component.literal("Usage: /heal <amount> or /heal <entities> <amount>"));
                    return 0;
                }));
    }

    private static int setCreativeMode(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer player = context.getSource().getPlayerOrException();
        player.setGameMode(GameType.CREATIVE);
        return 1;
    }

    private static int setSurvivalMode(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer player = context.getSource().getPlayerOrException();
        player.setGameMode(GameType.SURVIVAL);
        return 1;
    }

    private static int healEntities(CommandContext<CommandSourceStack> context, Collection<? extends Entity> entities, int amount) {
        final int[] healedCount = {0};

        for (Entity entity : entities) {
            if (entity instanceof LivingEntity livingEntity) {
                livingEntity.heal(amount);
                healedCount[0]++;
            }
        }

        context.getSource().sendSuccess(() -> Component.literal("Healed " + healedCount[0] + " entities by " + amount + " health."), true);
        return Command.SINGLE_SUCCESS;
    }

    private static int healSelf(CommandContext<CommandSourceStack> context, int amount) {
        try {
            ServerPlayer player = context.getSource().getPlayerOrException();
            player.heal(amount);
            return Command.SINGLE_SUCCESS;
        } catch (CommandSyntaxException commandSyntaxException) {
            return 0;
        }
    }
}
