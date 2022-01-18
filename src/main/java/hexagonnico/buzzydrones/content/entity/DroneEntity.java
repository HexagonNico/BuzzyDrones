package hexagonnico.buzzydrones.content.entity;

import hexagonnico.buzzydrones.content.entity.ai.EnterSourceGoal;
import hexagonnico.buzzydrones.content.entity.ai.EnterTargetGoal;
import hexagonnico.buzzydrones.content.entity.ai.FindIdleGoal;
import hexagonnico.buzzydrones.content.entity.ai.FindItemsGoal;
import hexagonnico.buzzydrones.content.entity.ai.FindSourceGoal;
import hexagonnico.buzzydrones.content.entity.ai.FindTargetGoal;
import hexagonnico.buzzydrones.content.entity.ai.FindTargetWithFilterGoal;
import hexagonnico.buzzydrones.registry.BuzzyDronesEntities;
import hexagonnico.buzzydrones.registry.BuzzyDronesItems;
import hexagonnico.buzzydrones.utils.NbtHelper;

import net.minecraft.block.Block;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.controller.FlyingMovementController;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.RedstoneParticleData;
import net.minecraft.pathfinding.FlyingPathNavigator;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

public class DroneEntity extends CreatureEntity {

	private static final DataParameter<Integer> STATUS = EntityDataManager.defineId(DroneEntity.class, DataSerializers.INT);

	private static final IParticleData BLUE_PARTICLES = new RedstoneParticleData(0.0f, 0.84f, 0.89f, 1.0f);
	private static final IParticleData GREEN_PARTICLES = new RedstoneParticleData(0.0f, 0.89f, 0.35f, 1.0f);
	private static final IParticleData ORANGE_PARTICLES = new RedstoneParticleData(0.89f, 0.35f, 0.0f, 1.0f);
	private static final IParticleData RED_PARTICLES = new RedstoneParticleData(0.89f, 0.0f, 0.09f, 1.0f);

	public static final int BASIC = 1;
	public static final int PICK_UP = 2;

	private ItemStack carrying = ItemStack.EMPTY;
	private int type;

	public DroneEntity(EntityType<? extends CreatureEntity> type, World world) {
		super(type, world);
		super.moveControl = new FlyingMovementController(this, 20, true);
		this.setPathfindingMalus(PathNodeType.DANGER_FIRE, -1.0F);
		this.setPathfindingMalus(PathNodeType.WATER, -1.0F);
		this.setPathfindingMalus(PathNodeType.WATER_BORDER, 16.0F);
		this.setPathfindingMalus(PathNodeType.COCOA, -1.0F);
		this.setPathfindingMalus(PathNodeType.FENCE, -1.0F);
	}

	public DroneEntity(World world, int type) {
		this(BuzzyDronesEntities.DRONE.get(), world);
		this.type = type;
		this.registerGoals();
	}

	public DroneEntity(DroneEntity copy) {
		this(copy.level, copy.type);
		this.carrying = copy.carrying;
		this.setStatus(copy.getStatus());
	}

	@Override
	protected void registerGoals() {
		if(this.type == BASIC) {
			this.goalSelector.addGoal(1, new FindSourceGoal(this));
			this.goalSelector.addGoal(2, new EnterSourceGoal(this));
			this.goalSelector.addGoal(3, new FindTargetWithFilterGoal(this));
			this.goalSelector.addGoal(4, new FindTargetGoal(this));
			this.goalSelector.addGoal(5, new EnterTargetGoal(this));
			this.goalSelector.addGoal(6, new FindIdleGoal(this));
		} else if(this.type == PICK_UP) {
			this.goalSelector.addGoal(1, new FindItemsGoal(this));
			this.goalSelector.addGoal(2, new FindTargetWithFilterGoal(this));
			this.goalSelector.addGoal(3, new FindTargetGoal(this));
			this.goalSelector.addGoal(4, new EnterTargetGoal(this));
			this.goalSelector.addGoal(5, new FindIdleGoal(this));
		}
	}

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		this.entityData.define(STATUS, Status.ERROR.index);
	}

	protected boolean canDespawn() {
        return false;
    }

	public boolean isCarryingItems() {
		return !this.carrying.isEmpty();
	}

	public boolean pickUpItems(ItemStack itemStack) {
		if(!itemStack.isEmpty() && this.carrying.getCount() < 64) {
			if(this.carrying.isEmpty()) {
				this.carrying = new ItemStack(itemStack.getItem(), 1);
				itemStack.shrink(1);
				return true;
			} else if(this.carrying.getItem().equals(itemStack.getItem())) {
				this.carrying.grow(1);
				itemStack.shrink(1);
				return true;
			}
		}
		return false;
	}

	public void pickUpAllItems(ItemStack itemStack) {
		if(!itemStack.isEmpty() && this.carrying.isEmpty()) {
			this.carrying = new ItemStack(itemStack.getItem(), itemStack.getCount());
			this.level.playSound(null, this.position().x(), this.position().y(), this.position().z(), SoundEvents.ITEM_PICKUP, SoundCategory.NEUTRAL, 1.0F, 1.0F);
			itemStack.setCount(0);
		}
	}

	public ItemStack getItemCarried() {
		return this.carrying;
	}

	public void setItemCarried(ItemStack itemStack) {
		this.carrying = itemStack;
	}

	public void decreaseItemCarriedCount() {
		this.carrying.setCount(this.carrying.getCount() - 1);
	}

	public boolean isFull() {
		return this.carrying.getCount() == this.carrying.getItem().getItemStackLimit(this.carrying);
	}

	public void setStatus(Status status) {
		this.entityData.set(STATUS, status.index);
	}

	public Status getStatus() {
		return Status.values()[this.entityData.get(STATUS)];
	}

	@Override
	public void baseTick() {
		super.baseTick();
		if(this.level.isClientSide) {
			this.level.addParticle(this.getParticles(), this.position().x(), this.position().y() + 0.4, this.position().z(), 0.1, 0.1, 0.1);
		}
	}

	@Override
	public void die(DamageSource cause) {
		super.die(cause);
		this.dropItemCarried(this.position());
		if(!cause.isCreativePlayer() || this.hasCustomName()) {
			ItemStack item = new ItemStack(this.type == BASIC ? BuzzyDronesItems.DRONE.get() : BuzzyDronesItems.DRONE_PICK_UP.get());
			if(this.hasCustomName()) item.setHoverName(this.getCustomName());
			Block.popResource(this.level, this.blockPosition(), item);
		}
	}

	public void dropItemCarried(Vector3d pos) {
		if(!this.carrying.isEmpty() && !this.level.isClientSide) {
			this.level.addFreshEntity(new ItemEntity(this.level, pos.x(), pos.y(), pos.z(), this.carrying));
		}
	}

	public void dropItemCarried(BlockPos pos) {
		this.dropItemCarried(new Vector3d(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5));
	}

	@Override
	public void addAdditionalSaveData(CompoundNBT compound) {
		super.addAdditionalSaveData(compound);
		this.writeInterestingData(compound);
	}

	@Override
	public void readAdditionalSaveData(CompoundNBT compound) {
		super.readAdditionalSaveData(compound);
		this.carrying = NbtHelper.loadSingleItem(compound, "Carrying");
		this.type = compound.getInt("DroneType");
		this.registerGoals();
	}

	public void writeInterestingData(CompoundNBT compound) {
		NbtHelper.saveSingleItem(compound, this.carrying, "Carrying");
		compound.putInt("DroneType", this.type);
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
		return SoundEvents.ARMOR_STAND_HIT;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return SoundEvents.ARMOR_STAND_BREAK;
	}

	@Override
	public boolean causeFallDamage(float f1, float f2) {
		return false;
	}

	@Override
	protected PathNavigator createNavigation(World worldIn) {
		FlyingPathNavigator flyingPathNavigator = new FlyingPathNavigator(this, worldIn);
		flyingPathNavigator.setCanOpenDoors(false);
		flyingPathNavigator.setCanFloat(false);
		flyingPathNavigator.setCanOpenDoors(true);
		return flyingPathNavigator;
	}

	public static AttributeModifierMap.MutableAttribute setAttributes() {
		return MobEntity.createMobAttributes()
				.add(Attributes.MAX_HEALTH, 3.0)
				.add(Attributes.FLYING_SPEED, 0.8);
	}

	private IParticleData getParticles() {
		switch(this.getStatus()) {
			case WORKING:
				return BLUE_PARTICLES;
			case IDLE:
				return GREEN_PARTICLES;
			case WARNING:
				return ORANGE_PARTICLES;
			case ERROR:
			default:
				return RED_PARTICLES;
		}
	}

	public enum Status {
		WORKING(0),
		IDLE(1),
		WARNING(2),
		ERROR(3);

		public int index;

		Status(int index) {
			this.index = index;
		}
	}
}
