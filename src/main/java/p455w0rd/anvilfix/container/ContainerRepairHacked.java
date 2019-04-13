package p455w0rd.anvilfix.container;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import net.minecraft.block.BlockAnvil;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemEnchantedBook;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import p455w0rd.anvilfix.init.ModConfig.Options;

/**
 * @author p455w0rd
 *
 */
public class ContainerRepairHacked extends ContainerRepair {

	/** Here comes out item you merged and/or renamed. */
	private final IInventory outputSlot;
	/** The 2slots where you put your items in that you want to merge and/or rename. */
	private final IInventory inputSlots;
	private final World world;
	private final BlockPos selfPosition;
	/** The maximum cost of repairing/renaming in the anvil. */
	public int maximumCost;
	/** determined by damage of input item and stackSize of repair materials */
	public int materialCost;
	private String repairedItemName;
	/** The player that has this container open. */
	private final EntityPlayer player;

	@SideOnly(Side.CLIENT)
	public ContainerRepairHacked(final World worldIn, final EntityPlayer player) {
		this(worldIn, BlockPos.ORIGIN, player);
	}

	public ContainerRepairHacked(final World worldIn, final BlockPos blockPosIn, final EntityPlayer player) {
		super(player.inventory, worldIn, blockPosIn, player);
		outputSlot = new InventoryCraftResult();
		inputSlots = new InventoryBasic("Repair", true, 2) {
			/**
			 * For tile entities, ensures the chunk containing the tile entity is saved to disk later - the game won't
			 * think it hasn't changed and skip it.
			 */
			@Override
			public void markDirty() {
				super.markDirty();
				ContainerRepairHacked.this.onCraftMatrixChanged(this);
			}
		};
		selfPosition = blockPosIn;
		world = worldIn;
		this.player = player;
		addSlot(new Slot(inputSlots, 0, 27, 47));
		addSlot(new Slot(inputSlots, 1, 76, 47));
		addSlot(new Slot(outputSlot, 2, 134, 47) {
			/**
			 * Check if the stack is allowed to be placed in this slot, used for armor slots as well as furnace fuel.
			 */
			@Override
			public boolean isItemValid(final ItemStack stack) {
				return false;
			}

			/**
			 * Return whether this slot's stack can be taken from this slot.
			 */
			@Override
			public boolean canTakeStack(final EntityPlayer playerIn) {
				return (playerIn.capabilities.isCreativeMode || playerIn.experienceLevel >= maximumCost) && maximumCost > 0 && getHasStack();
			}

			@Override
			public ItemStack onTake(final EntityPlayer thePlayer, final ItemStack stack) {
				if (!thePlayer.capabilities.isCreativeMode) {
					thePlayer.addExperienceLevel(-maximumCost);
				}

				final float breakChance = net.minecraftforge.common.ForgeHooks.onAnvilRepair(thePlayer, stack, inputSlots.getStackInSlot(0), inputSlots.getStackInSlot(1));

				inputSlots.setInventorySlotContents(0, ItemStack.EMPTY);

				if (materialCost > 0) {
					final ItemStack itemstack = inputSlots.getStackInSlot(1);

					if (!itemstack.isEmpty() && itemstack.getCount() > materialCost) {
						itemstack.shrink(materialCost);
						inputSlots.setInventorySlotContents(1, itemstack);
					}
					else {
						inputSlots.setInventorySlotContents(1, ItemStack.EMPTY);
					}
				}
				else {
					inputSlots.setInventorySlotContents(1, ItemStack.EMPTY);
				}

				maximumCost = 0;
				final IBlockState iblockstate = worldIn.getBlockState(blockPosIn);

				if (!Options.noAnvilDamage && !thePlayer.capabilities.isCreativeMode && !worldIn.isRemote && iblockstate.getBlock() == Blocks.ANVIL && thePlayer.getRNG().nextFloat() < breakChance) {
					int l = iblockstate.getValue(BlockAnvil.DAMAGE).intValue();
					++l;

					if (l > 2) {
						worldIn.setBlockToAir(blockPosIn);
						worldIn.playEvent(1029, blockPosIn, 0);
					}
					else {
						worldIn.setBlockState(blockPosIn, iblockstate.withProperty(BlockAnvil.DAMAGE, Integer.valueOf(l)), 2);
						worldIn.playEvent(1030, blockPosIn, 0);
					}
				}
				else if (!worldIn.isRemote) {
					worldIn.playEvent(1030, blockPosIn, 0);
				}

				return stack;
			}
		});

		for (int i = 0; i < 3; ++i) {
			for (int j = 0; j < 9; ++j) {
				addSlot(new Slot(player.inventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
			}
		}

		for (int k = 0; k < 9; ++k) {
			addSlot(new Slot(player.inventory, k, 8 + k * 18, 142));
		}
	}

	@Override
	protected Slot addSlotToContainer(final Slot slotIn) {
		return null;
	}

	private void addSlot(final Slot slot) {
		super.addSlotToContainer(slot);
	}

	/**
	 * Callback for when the crafting matrix is changed.
	 */
	@Override
	public void onCraftMatrixChanged(final IInventory inventoryIn) {
		if (inventoryIn == inputSlots) {
			updateRepairOutput();
		}
	}

	/**
	 * called when the Anvil Input Slot changes, calculates the new result and puts it in the output slot
	 */
	@Override
	public void updateRepairOutput() {
		final ItemStack itemstack = inputSlots.getStackInSlot(0);
		maximumCost = 1;
		int i = 0;
		int j = 0;
		int k = 0;

		if (itemstack.isEmpty()) {
			outputSlot.setInventorySlotContents(0, ItemStack.EMPTY);
			maximumCost = 0;
		}
		else {
			ItemStack itemstack1 = itemstack.copy();
			final ItemStack itemstack2 = inputSlots.getStackInSlot(1);
			final Map<Enchantment, Integer> map = EnchantmentHelper.getEnchantments(itemstack1);
			j = j + itemstack.getRepairCost() + (itemstack2.isEmpty() ? 0 : itemstack2.getRepairCost());
			materialCost = 0;
			boolean flag = false;

			if (!itemstack2.isEmpty()) {
				if (!net.minecraftforge.common.ForgeHooks.onAnvilChange(this, itemstack, itemstack2, outputSlot, repairedItemName, j)) {
					return;
				}
				flag = itemstack2.getItem() == Items.ENCHANTED_BOOK && !ItemEnchantedBook.getEnchantments(itemstack2).hasNoTags();

				if (itemstack1.isItemStackDamageable() && itemstack1.getItem().getIsRepairable(itemstack, itemstack2)) {
					int l2 = Math.min(itemstack1.getItemDamage(), itemstack1.getMaxDamage() / 4);

					if (l2 <= 0) {
						outputSlot.setInventorySlotContents(0, ItemStack.EMPTY);
						maximumCost = 0;
						return;
					}

					int i3;

					for (i3 = 0; l2 > 0 && i3 < itemstack2.getCount(); ++i3) {
						final int j3 = itemstack1.getItemDamage() - l2;
						itemstack1.setItemDamage(j3);
						++i;
						l2 = Math.min(itemstack1.getItemDamage(), itemstack1.getMaxDamage() / 4);
					}

					materialCost = i3;
				}
				else {
					if (!flag && (itemstack1.getItem() != itemstack2.getItem() || !itemstack1.isItemStackDamageable())) {
						outputSlot.setInventorySlotContents(0, ItemStack.EMPTY);
						maximumCost = 0;
						return;
					}

					if (itemstack1.isItemStackDamageable() && !flag) {
						final int l = itemstack.getMaxDamage() - itemstack.getItemDamage();
						final int i1 = itemstack2.getMaxDamage() - itemstack2.getItemDamage();
						final int j1 = i1 + itemstack1.getMaxDamage() * 12 / 100;
						final int k1 = l + j1;
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

					final Map<Enchantment, Integer> map1 = EnchantmentHelper.getEnchantments(itemstack2);
					boolean flag2 = false;
					boolean flag3 = false;

					for (final Enchantment enchantment1 : map1.keySet()) {
						if (enchantment1 != null) {
							final int i2 = map.containsKey(enchantment1) ? map.get(enchantment1).intValue() : 0;
							int j2 = map1.get(enchantment1).intValue();
							j2 = i2 == j2 ? j2 + 1 : Math.max(j2, i2);
							boolean flag1 = enchantment1.canApply(itemstack);

							if (player.capabilities.isCreativeMode || itemstack.getItem() == Items.ENCHANTED_BOOK) {
								flag1 = true;
							}

							for (final Enchantment enchantment : map.keySet()) {
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
						outputSlot.setInventorySlotContents(0, ItemStack.EMPTY);
						maximumCost = 0;
						return;
					}
				}
			}

			if (StringUtils.isBlank(repairedItemName)) {
				if (itemstack.hasDisplayName()) {
					k = 1;
					i += k;
					itemstack1.clearCustomName();
				}
			}
			else if (!repairedItemName.equals(itemstack.getDisplayName())) {
				k = 1;
				i += k;
				itemstack1.setStackDisplayName(repairedItemName);
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

			if (maximumCost >= Options.levelLimit && !player.capabilities.isCreativeMode) {
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

			outputSlot.setInventorySlotContents(0, itemstack1);
			detectAndSendChanges();
		}
	}

	@Override
	public void addListener(final IContainerListener listener) {
		super.addListener(listener);
		listener.sendWindowProperty(this, 0, maximumCost);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void updateProgressBar(final int id, final int data) {
		if (id == 0) {
			maximumCost = data;
		}
	}

	/**
	 * Called when the container is closed.
	 */
	@Override
	public void onContainerClosed(final EntityPlayer playerIn) {
		super.onContainerClosed(playerIn);

		if (!world.isRemote) {
			clearContainer(playerIn, world, inputSlots);
		}
	}

	/**
	 * Determines whether supplied player can use this container
	 */
	@Override
	public boolean canInteractWith(final EntityPlayer playerIn) {
		if (world.getBlockState(selfPosition).getBlock() != Blocks.ANVIL) {
			return false;
		}
		else {
			return playerIn.getDistanceSq(selfPosition.getX() + 0.5D, selfPosition.getY() + 0.5D, selfPosition.getZ() + 0.5D) <= 64.0D;
		}
	}

	/**
	 * Handle when the stack in slot {@code index} is shift-clicked. Normally this moves the stack between the player
	 * inventory and the other inventory(s).
	 */
	@Override
	public ItemStack transferStackInSlot(final EntityPlayer playerIn, final int index) {
		ItemStack itemstack = ItemStack.EMPTY;
		final Slot slot = inventorySlots.get(index);

		if (slot != null && slot.getHasStack()) {
			final ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();

			if (index == 2) {
				if (!mergeItemStack(itemstack1, 3, 39, true)) {
					return ItemStack.EMPTY;
				}

				slot.onSlotChange(itemstack1, itemstack);
			}
			else if (index != 0 && index != 1) {
				if (index >= 3 && index < 39 && !mergeItemStack(itemstack1, 0, 2, false)) {
					return ItemStack.EMPTY;
				}
			}
			else if (!mergeItemStack(itemstack1, 3, 39, false)) {
				return ItemStack.EMPTY;
			}

			if (itemstack1.isEmpty()) {
				slot.putStack(ItemStack.EMPTY);
			}
			else {
				slot.onSlotChanged();
			}

			if (itemstack1.getCount() == itemstack.getCount()) {
				return ItemStack.EMPTY;
			}

			slot.onTake(playerIn, itemstack1);
		}

		return itemstack;
	}

	/**
	 * used by the Anvil GUI to update the Item Name being typed by the player
	 */
	@Override
	public void updateItemName(final String newName) {
		repairedItemName = newName;

		if (getSlot(2).getHasStack()) {
			final ItemStack itemstack = getSlot(2).getStack();

			if (StringUtils.isBlank(newName)) {
				itemstack.clearCustomName();
			}
			else {
				itemstack.setStackDisplayName(repairedItemName);
			}
		}

		updateRepairOutput();
	}
}
