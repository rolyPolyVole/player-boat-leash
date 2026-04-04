package dev.rolypolyvole.playerboatleash.mixin;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.vehicle.boat.AbstractBoat;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class EntityMixin {

    @Unique
    private boolean playerBoatLeash$isEntityValid() {
        if (!(((Entity)(Object) this) instanceof AbstractBoat boat)) return false;

        return boat.isLeashed();
    }

    @Inject(method = "isClientAuthoritative", at = @At("HEAD"), cancellable = true)
    private void setServerControlled(CallbackInfoReturnable<Boolean> info) {
        if (playerBoatLeash$isEntityValid()) {
            info.setReturnValue(false);
        }
    }

    @Inject(method = "getKnownMovement", at = @At("HEAD"), cancellable = true)
    private void preventMovementOverride(CallbackInfoReturnable<Vec3> info) {
        Entity self = (Entity) ((Object) this);

        if (playerBoatLeash$isEntityValid()) {
            info.setReturnValue(self.getDeltaMovement());
        }
    }
}
