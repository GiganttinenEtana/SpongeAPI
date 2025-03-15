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
package org.spongepowered.api.event.lifecycle;

import org.spongepowered.api.Engine;
import org.spongepowered.api.ResourceKey;
import org.spongepowered.api.event.GenericEvent;
import org.spongepowered.api.registry.Registry;
import org.spongepowered.api.registry.RegistryHolder;
import org.spongepowered.api.registry.RegistryType;

import java.util.function.Consumer;

public interface FreezeRegistryEvent extends LifecycleEvent {

    /**
     * Fired after a layer has established its registries
     * and is no longer accepting any type of modifications.
     *
     * <p><strong>Note:</strong> Layers might be reloadable!
     * When a registry is being reloaded, this event is fired
     * again for the relevant registries.</p>
     */
    interface Post extends FreezeRegistryEvent {

        /**
         * Gets the built {@link RegistryHolder registry holder}.
         *
         * @return The registry holder.
         */
        RegistryHolder holder();

        /**
         * <p>Fetch a registry by its type.</p>
         *
         * <p>The consumer will be called if it matches the current set of
         * registries being created.</p>
         *
         * @param registryType The registry type to fetch.
         * @param consumer The consumer to be called if found.
         */
        default <T> void registry(RegistryType<T> registryType, Consumer<Registry<T>> consumer) {
            this.holder().findRegistry(registryType).ifPresent(consumer);
        }

        interface GameScoped extends Post {
        }

        interface EngineScoped<E extends Engine> extends Post, GenericEvent<E> {
        }

        interface WorldScoped extends Post {

            ResourceKey worldKey();
        }
    }
}
