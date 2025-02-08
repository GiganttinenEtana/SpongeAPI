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

import org.spongepowered.api.registry.DefaultedRegistryReference;
import org.spongepowered.api.registry.RegistryKey;
import org.spongepowered.api.tag.Tag;

import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * Lifecycle event for applying modifications to {@link Tag tags}.
 *
 * <p>This event provides mechanism for plugins to <strong>propose</strong>
 * changes to the tags that are processed during tag construction.</p>
 *
 * <p>The following chain of events take place during tag construction:</p>
 * <ul>
 *     <li>Tags are merged from data packs</li>
 *     <li>Append proposed tags</li>
 *     <li>Apply filter to ALL present tags</li>
 *     <li>Remaining items makes up the final tag</li>
 * </ul>
 */
public interface RegisterTagEvent extends LifecycleEvent {

    <T> TagStep<T> tag(Tag<T> tag);

    interface TagStep<T> {

        default TagStep<T> delete() {
            return this.filter(k -> false);
        }

        TagStep<T> filter(Predicate<DefaultedRegistryReference<T>> predicate);

        default TagStep<T> filterTags(Predicate<Tag<T>> predicate) {
            return this.filterTags((t, k) -> predicate.test(t));
        }

        TagStep<T> filterTags(BiPredicate<Tag<T>, DefaultedRegistryReference<T>> predicate);

        TagStep<T> append(RegistryKey<T> key);

        TagStep<T> append(Tag<T> tag);

        TagStep<T> test(Predicate<DefaultedRegistryReference<T>> predicate, Consumer<TagStep<T>> consumer);

        default TagStep<T> testTags(Predicate<Tag<T>> predicate, Consumer<TagStep<T>> consumer) {
            return this.testTags((t, k) -> predicate.test(t), consumer);
        }

        TagStep<T> testTags(BiPredicate<Tag<T>, DefaultedRegistryReference<T>> predicate, Consumer<TagStep<T>> consumer);
    }
}
