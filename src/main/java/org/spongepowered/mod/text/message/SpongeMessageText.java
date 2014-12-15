/**
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
package org.spongepowered.mod.text.message;

import java.util.ArrayList;
import java.util.Iterator;

import net.minecraft.util.ChatComponentText;

import org.spongepowered.api.text.action.ClickAction;
import org.spongepowered.api.text.action.HoverAction;
import org.spongepowered.api.text.action.ShiftClickAction;
import org.spongepowered.api.text.format.TextColor;
import org.spongepowered.api.text.format.TextStyle;
import org.spongepowered.api.text.format.TextStyle.TextStyleComponent;
import org.spongepowered.api.text.format.TextStyles;
import org.spongepowered.api.text.message.Message;
import org.spongepowered.api.text.message.MessageBuilder;
import org.spongepowered.mod.registry.SpongeGameRegistry;

import com.google.common.base.Optional;

public class SpongeMessageText extends SpongeMessage implements Message.Text {

    @Override
    public MessageBuilder.Text builder() {
        return new SpongeTextBuilder(this.content);
    }

    private SpongeMessageText(SpongeTextBuilder builder) {
        super(builder);
    }

    public static class SpongeTextBuilder extends SpongeMessageBuilder<Text> implements MessageBuilder.Text {

        protected String content;
        protected ChatComponentText handle;

        public SpongeTextBuilder() {
            children = new ArrayList<Message>();
            handle = new ChatComponentText("");
        }

        public SpongeTextBuilder(String text) {
            this.content = text;
            children = new ArrayList<Message>();
            handle = new ChatComponentText(content);
        }

        public MessageBuilder.Text append(Message message) {
            this.children.add(message);
            this.handle.appendSibling(((SpongeMessageText)message).handle);
            return this;
        }

        @Override
        public MessageBuilder.Text append(Message... children) {
            for (Message message : children) {
                this.children.add(message);
                this.handle.appendSibling(((SpongeMessageText)message).handle);
            }

            return this;
        }

        @Override
        public MessageBuilder.Text append(Iterable<Message> children) {
            Iterator<Message> iter = children.iterator();
            while (iter.hasNext()) {
                Message message = iter.next();
                this.children.add(message);
                this.handle.appendSibling(((SpongeMessageText)message).handle);
            }

            return this;
        }

        public MessageBuilder.Text content(String content) {
            this.content = content;
            handle = new ChatComponentText(content);
            return this;
        }

        @Override
        public MessageBuilder.Text color(TextColor color) {
            this.color = color;
            this.handle.getChatStyle().setColor(SpongeGameRegistry.textColorToEnumMappings.get(color));
            return this;
        }

        @Override
        public MessageBuilder.Text style(TextStyle... styles) {
            TextStyle style = TextStyles.ZERO.and(styles);
            for (TextStyle.Base baseStyle : SpongeGameRegistry.textStyleMappings.values()) {
                TextStyleComponent component = style.applied(baseStyle);

                if (component != TextStyleComponent.UNAPPLIED) {
                    if (baseStyle == TextStyles.BOLD) {
                        if (component == TextStyleComponent.APPLIED) {
                            this.handle.getChatStyle().setBold(true);
                        } else {
                            this.handle.getChatStyle().setBold(false);
                        }
                    } else if (baseStyle == TextStyles.ITALIC) {
                        if (component == TextStyleComponent.APPLIED) {
                            this.handle.getChatStyle().setItalic(true);
                        } else {
                            this.handle.getChatStyle().setItalic(false);
                        }
                    } else if (baseStyle == TextStyles.OBFUSCATED) {
                        if (component == TextStyleComponent.APPLIED) {
                            this.handle.getChatStyle().setObfuscated(true);
                        } else {
                            this.handle.getChatStyle().setObfuscated(false);
                        }
                    } else if (baseStyle == TextStyles.STRIKETHROUGH) {
                        if (component == TextStyleComponent.APPLIED) {
                            this.handle.getChatStyle().setStrikethrough(true);
                        } else {
                            this.handle.getChatStyle().setStrikethrough(false);
                        }
                    } else if (baseStyle == TextStyles.UNDERLINE) {
                        if (component == TextStyleComponent.APPLIED) {
                            this.handle.getChatStyle().setUnderlined(true);
                        } else {
                            this.handle.getChatStyle().setUnderlined(false);
                        }
                    } else if (baseStyle == TextStyles.RESET) {
                        // TODO
                    }
                }
            }
            return this;
        }

        @Override
        public MessageBuilder.Text onClick(ClickAction<?> action) {
            this.clickAction = Optional.<ClickAction<?>>fromNullable(action);
            // TODO
            return this;
        }

        @Override
        public MessageBuilder.Text onHover(HoverAction<?> action) {
            this.hoverAction = Optional.<HoverAction<?>>fromNullable(action);
            // TODO
            return this;
        }

        @Override
        public MessageBuilder.Text onShiftClick(ShiftClickAction<?> action) {
            this.shiftClickAction = Optional.<ShiftClickAction<?>>fromNullable(action);
            // TODO
            return this;
        }

        @Override
        public Message.Text build() {
            return new SpongeMessageText(this);
        }
    }

}
