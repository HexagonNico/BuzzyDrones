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

import com.mojang.math.Vector3f;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.Vec3;

public class DroneEntity extends PathfinderMob {

	private static final EntityDataAccessor<Integer> STATUS = SynchedEntityData.defineId(DroneEntity.class, EntityDataSerializers.INT);

	private static final ParticleOptions BLUE_PARTICLES = new DustParticleOptions(new Vector3f(0.0f, 0.84f, 0.89f), 1.0f);
	private static final ParticleOptions GREEN_PARTICLES = new DustParticleOptions(new Vector3f(0.0f, 0.89f, 0.35f), 1.0f);
	private static final ParticleOptions ORANGE_PARTICLES = new DustParticleOptions(new Vector3f(0.89f, 0.35f, 0.0f), 1.0f);
	private static final ParticleOptions RED_PARTICLES = new DustParticleOptions(new Vector3f(0.89f, 0.0f, 0.09f), 1.0f);

	public static final int BASIC = 1;
	public static final int PICK_UP = 2;

	private ItemStack carrying = ItemStack.EMPTY;
	private int type;

	public DroneEntity(EntityType<? extends PathfinderMob> type, Level level) {
		super(type, level);
		super.moveControl = new FlyingMoveControl(this, 20, true);
		this.setPathfindingMalus(BlockPathTypes.DANGER_FIRE, -1.0F);
		this.setPathfindingMalus(BlockPathTypes.WATER, -1.0F);
		this.setPathfindingMalus(BlockPathTypes.WATER_BORDER, 16.0F);
		this.setPathfindingMalus(BlockPathTypes.COCOA, -1.0F);
		this.setPathfindingMalus(BlockPathTypes.FENCE, -1.0F);
	}

	public DroneEntity(Level level, int type) {
		this(BuzzyDronesEntities.DRONE.get(), level);
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
			this.level.playSound(null, this.position().x(), this.position().y(), this.position().z(), SoundEvents.ITEM_PICKUP, SoundSource.NEUTRAL, 1.0F, 1.0F);
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

	public void dropItemCarried(Vec3 pos) {
		if(!this.carrying.isEmpty() && !this.level.isClientSide) {
			this.level.addFreshEntity(new ItemEntity(this.level, pos.x, pos.y, pos.z, this.carrying));
		}
	}

	public void dropItemCarried(BlockPos pos) {
		this.dropItemCarried(new Vec3(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5));
	}

	@Override
	public void addAdditionalSaveData(CompoundTag compound) {
		super.addAdditionalSaveData(compound);
		this.writeInterestingData(compound);
	}

	@Override
	public void readAdditionalSaveData(CompoundTag compound) {
		super.readAdditionalSaveData(compound);
		this.carrying = NbtHelper.loadSingleItem(compound, "Carrying");
		this.type = compound.getInt("DroneType");
		this.registerGoals();
	}

	public void writeInterestingData(CompoundTag compound) {
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
	public boolean causeFallDamage(float f1, float f2, DamageSource source) {
		return false;
	}

	@Override
	protected PathNavigation createNavigation(Level level) {
		FlyingPathNavigation flyingPathNavigation = new FlyingPathNavigation(this, level);
		flyingPathNavigation.setCanOpenDoors(false);
		flyingPathNavigation.setCanFloat(false);
		flyingPathNavigation.setCanOpenDoors(true);
		return flyingPathNavigation;
	}

	public static AttributeSupplier.Builder setAttributes() {
		return Mob.createMobAttributes()
				.add(Attributes.MAX_HEALTH, 3.0)
				.add(Attributes.FLYING_SPEED, 0.8);
	}

	private ParticleOptions getParticles() {
		return switch (this.getStatus()) {
			case WORKING -> BLUE_PARTICLES;
			case IDLE -> GREEN_PARTICLES;
			case WARNING -> ORANGE_PARTICLES;
			default -> RED_PARTICLES;
		};
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
