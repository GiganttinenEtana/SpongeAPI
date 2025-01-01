/*
 * This file is part of SpongeAPI, licensed under the MIT License (MIT).
 *
 * Copyright (c) SpongePowered <https://www.spongepowered.org>
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
package org.spongepowered.api.event;

import org.spongepowered.api.event.impl.AbstractCompositeEvent;
import org.spongepowered.eventgen.annotations.GenerateFactoryMethod;
import org.spongepowered.eventgen.annotations.ImplementedBy;
import org.spongepowered.eventgen.annotations.PropertySettings;

import java.util.List;
import java.util.function.Consumer;

/**
 * A {@link CompositeEvent} is an {@link Event} that contains multiple
 * side effectual {@link Event Events}, which may have their own side effects
 * and may be {@link Cancellable}. In some cases, the interactions of this event
 * may be cancellable as a whole, but are not guaranteed to revert all side
 * effects on the {@link org.spongepowered.api.Game}. The {@link #children()} of
 * this event are ordered in a "best-effort" basis, and may not be guaranteed
 * to be in any particular order.
 * <p>Using {@link #setCancelled(boolean)} will perform a best effort cancellation
 * on each of the children events.
 */
@GenerateFactoryMethod
@ImplementedBy(AbstractCompositeEvent.class)
public interface CompositeEvent<E extends Event> extends Event, Cancellable {

    @PropertySettings(useInToString = false)
    E baseEvent();

    List<Event> children();

    default <A extends Event> List<? extends A> event(Class<A> type) {
        return this.children().stream()
            .filter(type::isInstance)
            .map(type::cast)
            .toList();
    }

    default <A extends Event> void applyTo(Class<A> type, Consumer<? super A> consumer) {
        this.children().stream()
            .filter(type::isInstance)
            .map(type::cast)
            .forEach(consumer);
    }

    /**
     * {@inheritDoc}
     *
     * Cancels this event and all related events captured {@link #children()}.
     * Selectively, if individual events are wished to be cancelled,
     * the individual events should be cancelled instead.
     *
     * @param cancel The new cancelled state
     */
    @PropertySettings(generateMethods = false)
    @Override
    void setCancelled(boolean cancel);
}
