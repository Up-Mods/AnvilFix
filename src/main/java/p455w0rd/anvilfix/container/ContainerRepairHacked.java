package p455w0rd.anvilfix.container;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.ContainerRepair;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemEnchantedBook;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import p455w0rd.anvilfix.init.ModConfig.Options;

/**
 * @author p455w0rd
 *
 */
public class ContainerRepairHacked extends ContainerRepair {

	private final IInventory output;
	private final IInventory inputs;
	private String repairedName;
	private final EntityPlayer playerx;

	public ContainerRepairHacked(World world, BlockPos blockPos, EntityPlayer player) {
		super(player.inventory, world, blockPos, player);
		output = ObfuscationReflectionHelper.getPrivateValue(ContainerRepair.class, this, "outputSlot");
		inputs = ObfuscationReflectionHelper.getPrivateValue(ContainerRepair.class, this, "inputSlots");
		playerx = ObfuscationReflectionHelper.getPrivateValue(ContainerRepair.class, this, "player");
		repairedName = ObfuscationReflectionHelper.getPrivateValue(ContainerRepair.class, this, "repairedItemName");
	}

	@SideOnly(Side.CLIENT)
	public ContainerRepairHacked(World worldIn, EntityPlayer player) {
		this(worldIn, BlockPos.ORIGIN, player);
	}

	@Override
	public void updateRepairOutput() {
		ItemStack itemstack = inputs.getStackInSlot(0);
		maximumCost = 1;
		int i = 0;
		int j = 0;
		int k = 0;

		if (itemstack.isEmpty()) {
			output.setInventorySlotContents(0, ItemStack.EMPTY);
			maximumCost = 0;
		}
		else {
			ItemStack itemstack1 = itemstack.copy();
			ItemStack itemstack2 = inputs.getStackInSlot(1);
			Map<Enchantment, Integer> map = EnchantmentHelper.getEnchantments(itemstack1);
			j = j + itemstack.getRepairCost() + (itemstack2.isEmpty() ? 0 : itemstack2.getRepairCost());
			materialCost = 0;
			boolean flag = false;

			if (!itemstack2.isEmpty()) {
				if (!net.minecraftforge.common.ForgeHooks.onAnvilChange(this, itemstack, itemstack2, output, repairedName, j)) {
					return;
				}
				flag = itemstack2.getItem() == Items.ENCHANTED_BOOK && !ItemEnchantedBook.getEnchantments(itemstack2).hasNoTags();

				if (itemstack1.isItemStackDamageable() && itemstack1.getItem().getIsRepairable(itemstack, itemstack2)) {
					int l2 = Math.min(itemstack1.getItemDamage(), itemstack1.getMaxDamage() / 4);

					if (l2 <= 0) {
						output.setInventorySlotContents(0, ItemStack.EMPTY);
						maximumCost = 0;
						return;
					}

					int i3;

					for (i3 = 0; l2 > 0 && i3 < itemstack2.getCount(); ++i3) {
						int j3 = itemstack1.getItemDamage() - l2;
						itemstack1.setItemDamage(j3);
						++i;
						l2 = Math.min(itemstack1.getItemDamage(), itemstack1.getMaxDamage() / 4);
					}

					materialCost = i3;
				}
				else {
					if (!flag && (itemstack1.getItem() != itemstack2.getItem() || !itemstack1.isItemStackDamageable())) {
						output.setInventorySlotContents(0, ItemStack.EMPTY);
						maximumCost = 0;
						return;
					}

					if (itemstack1.isItemStackDamageable() && !flag) {
						int l = itemstack.getMaxDamage() - itemstack.getItemDamage();
						int i1 = itemstack2.getMaxDamage() - itemstack2.getItemDamage();
						int j1 = i1 + itemstack1.getMaxDamage() * 12 / 100;
						int k1 = l + j1;
						int l1 = itemstack1.getMaxDamage() - k1;

						if (l1 < 0) {
							l1 = 0;
						}

						if (l1 < itemstack1.getItemDamage()) // vanilla uses metadata here instead of damage.
						{
							itemstack1.setItemDamage(l1);
							i += 2;
						}
					}

					Map<Enchantment, Integer> map1 = EnchantmentHelper.getEnchantments(itemstack2);
					boolean flag2 = false;
					boolean flag3 = false;

					for (Enchantment enchantment1 : map1.keySet()) {
						if (enchantment1 != null) {
							int i2 = map.containsKey(enchantment1) ? map.get(enchantment1).intValue() : 0;
							int j2 = map1.get(enchantment1).intValue();
							j2 = i2 == j2 ? j2 + 1 : Math.max(j2, i2);
							boolean flag1 = true;//enchantment1.canApply(itemstack);

							if (playerx.capabilities.isCreativeMode || itemstack.getItem() == Items.ENCHANTED_BOOK) {
								flag1 = true;
							}

							for (Enchantment enchantment : map.keySet()) {
								if (enchantment != enchantment1 && !enchantment1.isCompatibleWith(enchantment)) {
									flag1 = false;
									++i;
								}
							}

							if (!flag1) {
								flag3 = true;
							}
							else {
								flag2 = true;

								if (j2 > enchantment1.getMaxLevel()) {
									j2 = enchantment1.getMaxLevel();
								}

								map.put(enchantment1, Integer.valueOf(j2));
								int k3 = 0;

								switch (enchantment1.getRarity()) {
								case COMMON:
									k3 = 1;
									break;
								case UNCOMMON:
									k3 = 2;
									break;
								case RARE:
									k3 = 4;
									break;
								case VERY_RARE:
									k3 = 8;
								}

								if (flag) {
									k3 = Math.max(1, k3 / 2);
								}

								i += k3 * j2;

								if (itemstack.getCount() > 1) {
									i = 40;
								}
							}
						}
					}

					if (flag3 && !flag2) {
						output.setInventorySlotContents(0, ItemStack.EMPTY);
						maximumCost = 0;
						return;
					}
				}
			}

			if (StringUtils.isBlank(repairedName)) {
				if (itemstack.hasDisplayName()) {
					k = 1;
					i += k;
					itemstack1.clearCustomName();
				}
			}
			else if (!repairedName.equals(itemstack.getDisplayName())) {
				k = 1;
				i += k;
				itemstack1.setStackDisplayName(repairedName);
			}
			if (flag && !itemstack1.getItem().isBookEnchantable(itemstack1, itemstack2)) {
				itemstack1 = ItemStack.EMPTY;
			}

			maximumCost = j + i;

			if (i <= 0) {
				itemstack1 = ItemStack.EMPTY;
			}

			if (k == i && k > 0 && maximumCost >= 40) {
				maximumCost = 39;
			}

			if (maximumCost >= Options.levelLimit && !playerx.capabilities.isCreativeMode) {
				itemstack1 = ItemStack.EMPTY;
			}

			if (!itemstack1.isEmpty()) {
				int k2 = itemstack1.getRepairCost();

				if (!itemstack2.isEmpty() && k2 < itemstack2.getRepairCost()) {
					k2 = itemstack2.getRepairCost();
				}

				if (k != i || k == 0) {
					k2 = k2 * 2 + 1;
				}

				itemstack1.setRepairCost(k2);
				EnchantmentHelper.setEnchantments(map, itemstack1);
			}

			output.setInventorySlotContents(0, itemstack1);
			detectAndSendChanges();
		}
	}

}
