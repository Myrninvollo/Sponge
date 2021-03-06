/*
 * This file is part of Sponge, licensed under the MIT License (MIT).
 *
 * Copyright (c) SpongePowered.org <http://www.spongepowered.org>
 * Copyright (c) contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.spongepowered.mod.mixin.core.block.data;

import static org.spongepowered.api.service.persistence.data.DataQuery.of;

import com.google.common.base.Optional;
import net.minecraft.command.server.CommandBlockLogic;
import net.minecraft.util.IChatComponent;
import org.spongepowered.api.block.tile.CommandBlock;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.service.persistence.data.DataContainer;
import org.spongepowered.api.service.persistence.data.DataQuery;
import org.spongepowered.api.util.annotation.NonnullByDefault;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.mod.text.SpongeChatComponent;
import org.spongepowered.mod.text.SpongeText;

@NonnullByDefault
@Implements(@Interface(iface = CommandBlock.class, prefix = "command$"))
@Mixin(net.minecraft.tileentity.TileEntityCommandBlock.class)
public abstract class MixinTileEntityCommandBlock extends MixinTileEntity {

    @Shadow
    public abstract CommandBlockLogic getCommandBlockLogic();

    void command$execute() {
        getCommandBlockLogic().trigger(this.worldObj);
    }

    @Override
    @SuppressWarnings("deprecated")
    public DataContainer toContainer() {
        DataContainer container = super.toContainer();
        container.set(of("StoredCommand"), this.getCommandBlockLogic().commandStored);
        container.set(of("SuccessCount"), this.getCommandBlockLogic().successCount);
        container.set(of("CustomName"), this.getCommandBlockLogic().getCustomName());
        container.set(of("DoesTrackOutput"), this.getCommandBlockLogic().shouldTrackOutput());
        if (this.getCommandBlockLogic().shouldTrackOutput()) {
//            Optional<Text> message = this.getCommandBlockLogic().getLastOutput();
//            if (message.isPresent()) {
//                container.set(of("TrackedOutput"), message.get().toString());
//            }
        }
        return container;
    }
}
