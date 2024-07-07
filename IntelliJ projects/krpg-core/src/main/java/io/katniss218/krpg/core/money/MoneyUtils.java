package io.katniss218.krpg.core.money;

import io.katniss218.krpg.core.KRPGCore;
import io.katniss218.krpg.core.utils.ColorUtils;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.text.DecimalFormat;

public class MoneyUtils
{
    private static final DecimalFormat decimalFormat = new DecimalFormat( "#.#" );

    public static double getMoney( @Nonnull Player player )
    {
        return KRPGCore.getVaultEconomy().getBalance( player );
    }

    static void modifyMoneySigned( @Nonnull Player player, double signedAmount )
    {
        var vaultEco = KRPGCore.getVaultEconomy();
        double playerBalance = vaultEco.getBalance( player );
        if( signedAmount > 0 )
        {
            vaultEco.depositPlayer( player, signedAmount );

            player.sendMessage( ColorUtils.GetComponent( "&aYou've gained ₡" + decimalFormat.format( signedAmount ) + " (" + decimalFormat.format( playerBalance ) + ")." ) );
            player.playSound( player.getLocation(), Sound.ITEM_ARMOR_EQUIP_CHAIN, 1.0f, 0.9f );
        }
        else if( signedAmount < 0 )
        {
            vaultEco.withdrawPlayer( player, -signedAmount );

            player.sendMessage( ColorUtils.GetComponent( "&aYou've lost ₡" + decimalFormat.format( signedAmount ) + " (" + decimalFormat.format( playerBalance ) + ")." ) );
        }
    }

    public static void addMoney( @Nonnull Player player, double amount )
    {
        modifyMoneySigned( player, amount );
    }

    public static void subtractMoney( @Nonnull Player player, double amount )
    {
        modifyMoneySigned( player, -amount );
    }
}