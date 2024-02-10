package nl.officialfox.kitac;

import org.bukkit.event.Event;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class MoneyTransferEvent extends Event {
    public static HandlerList handlers = new HandlerList();
    private Player sender;
    private Player receiver;
    private int amount;

    public MoneyTransferEvent(Player sender, Player receiver, int amount) {
        this.sender = sender;
        this.receiver = receiver;
        this.amount = amount;
    }

    public Player getSender() {
        return sender;
    }

    public Player getReceiver() {
        return receiver;
    }

    public int getAmount() {
        return amount;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

}