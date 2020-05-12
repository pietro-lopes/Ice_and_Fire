package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.DragonUtils;
import com.github.alexthe666.iceandfire.entity.EntityHippogryph;
import com.google.common.base.Predicate;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.PlayerEntity;

public class HippogryphAITarget<T extends LivingEntity> extends NearestAttackableTargetGoal<T> {
    private EntityHippogryph hippogryph;

    public HippogryphAITarget(EntityHippogryph entityIn, Class<T> classTarget, boolean checkSight, Predicate<? super T> targetSelector) {
        super(entityIn, classTarget, 20, checkSight, false, targetSelector);
        this.hippogryph = entityIn;
    }

    @Override
    public boolean shouldExecute() {
        if (this.taskOwner.getRNG().nextInt(20) != 0) {
            return false;
        }
        if (super.shouldExecute() && this.targetEntity != null && !this.targetEntity.getClass().equals(this.hippogryph.getClass())) {
            if (this.hippogryph.width >= this.targetEntity.width) {
                if (this.targetEntity instanceof PlayerEntity) {
                    return !hippogryph.isTamed();
                } else {
                    if (!hippogryph.isOwner(this.targetEntity) && hippogryph.canMove() && this.targetEntity instanceof EntityAnimal) {
                        if (hippogryph.isTamed()) {
                            return DragonUtils.canTameDragonAttack(hippogryph, this.targetEntity);
                        } else {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
}