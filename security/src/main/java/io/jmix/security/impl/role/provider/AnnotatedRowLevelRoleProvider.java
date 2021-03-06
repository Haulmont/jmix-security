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

package io.jmix.security.impl.role.provider;

import io.jmix.core.impl.scanning.JmixModulesClasspathScanner;
import io.jmix.security.impl.role.builder.AnnotatedRoleBuilder;
import io.jmix.security.model.RowLevelRole;
import io.jmix.security.role.RowLevelRoleProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Role provider that gets row level roles from classes annotated with
 * {@link io.jmix.security.role.annotation.RowLevelRole}.
 */
@Component("sec_AnnotatedRowLevelRoleProvider")
public class AnnotatedRowLevelRoleProvider implements RowLevelRoleProvider {

    protected Map<String, RowLevelRole> roles;

    @Autowired
    public AnnotatedRowLevelRoleProvider(JmixModulesClasspathScanner classpathScanner,
                                         AnnotatedRoleBuilder annotatedRoleBuilder) {
        Set<String> classNames = classpathScanner.getClassNames(RowLevelRoleDetector.class);
        roles = classNames.stream()
                .map(annotatedRoleBuilder::createRowLevelRole)
                .collect(Collectors.toMap(RowLevelRole::getCode, Function.identity()));
    }

    @Override
    public Collection<RowLevelRole> getAllRoles() {
        return roles.values();
    }

    @Override
    @Nullable
    public RowLevelRole findRoleByCode(String code) {
        return roles.get(code);
    }

    @Override
    public boolean deleteRole(RowLevelRole role) {
        throw new UnsupportedOperationException("Annotated RowLevelRole cannot be deleted");
    }
}
