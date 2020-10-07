/*
 * Copyright 2020 Haulmont.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.jmix.security.constraint;

import io.jmix.core.metamodel.model.MetaClass;
import io.jmix.security.model.ResourcePolicy;
import io.jmix.security.model.RowLevelPolicy;

import java.util.Collection;

public interface PolicyStore {

    Collection<RowLevelPolicy> getRowLevelPolicies(MetaClass entityClass);

    Collection<ResourcePolicy> getEntityResourcePolicies(MetaClass metaClass);

    Collection<ResourcePolicy> getEntityResourcePoliciesByWildcard(String wildcard);

    Collection<ResourcePolicy> getEntityAttributesResourcePolicies(MetaClass metaClass, String attribute);

    Collection<ResourcePolicy> getEntityAttributesResourcePoliciesByWildcard(String entityWildcard, String attributeWildcard);

    Collection<ResourcePolicy> getSpecificResourcePolicies(String resourceName);
}