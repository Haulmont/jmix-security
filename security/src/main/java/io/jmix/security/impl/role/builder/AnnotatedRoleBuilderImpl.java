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

package io.jmix.security.impl.role.builder;

import io.jmix.core.common.util.ReflectionHelper;
import io.jmix.security.impl.role.builder.extractor.ResourcePolicyExtractor;
import io.jmix.security.impl.role.builder.extractor.RowLevelPolicyExtractor;
import io.jmix.security.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Component("sec_AnnotatedRoleBuilder")
public class AnnotatedRoleBuilderImpl implements AnnotatedRoleBuilder {

    private final Collection<ResourcePolicyExtractor> resourcePolicyExtractors;
    private final Collection<RowLevelPolicyExtractor> rowLevelPolicyExtractors;

    @Autowired
    public AnnotatedRoleBuilderImpl(Collection<ResourcePolicyExtractor> resourcePolicyExtractors,
                                    Collection<RowLevelPolicyExtractor> rowLevelPolicyExtractors) {
        this.resourcePolicyExtractors = resourcePolicyExtractors;
        this.rowLevelPolicyExtractors = rowLevelPolicyExtractors;
    }

    @Override
    public ResourceRole createResourceRole(String className) {
        Class<?> roleClass = loadClass(className);

        io.jmix.security.role.annotation.ResourceRole roleAnnotation =
                roleClass.getAnnotation(io.jmix.security.role.annotation.ResourceRole.class);

        ResourceRole role = new ResourceRole();
        initBaseParameters(role, roleAnnotation.name(), roleAnnotation.code());
        role.setResourcePolicies(extractResourcePolicies(roleClass));

        return role;
    }

    @Override
    public RowLevelRole createRowLevelRole(String className) {
        Class<?> roleClass = loadClass(className);

        io.jmix.security.role.annotation.RowLevelRole roleAnnotation =
                roleClass.getAnnotation(io.jmix.security.role.annotation.RowLevelRole.class);

        RowLevelRole role = new RowLevelRole();
        initBaseParameters(role, roleAnnotation.name(), roleAnnotation.code());
        role.setRowLevelPolicies(extractRowLevelPolicies(roleClass));

        return role;
    }

    protected Class<?> loadClass(String className) {
        Class<?> clazz;
        try {
            clazz = ReflectionHelper.loadClass(className);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(String.format("Cannot find role class: %s", className));
        }
        return clazz;
    }

    protected void initBaseParameters(BaseRole role, String name, String code) {
        role.setName(name);
        role.setCode(code);
        role.setSource(RoleSource.ANNOTATED_CLASS);
    }

    protected Collection<ResourcePolicy> extractResourcePolicies(Class<?> roleClass) {
        List<ResourcePolicy> policies = new ArrayList<>();
        for (Method method : roleClass.getMethods()) {
            for (ResourcePolicyExtractor policyExtractor : resourcePolicyExtractors) {
                policies.addAll(policyExtractor.extractResourcePolicies(method));
            }
        }
        return policies;
    }

    protected Collection<RowLevelPolicy> extractRowLevelPolicies(Class<?> roleClass) {
        List<RowLevelPolicy> policies = new ArrayList<>();
        for (Method method : roleClass.getMethods()) {
            for (RowLevelPolicyExtractor policyExtractor : rowLevelPolicyExtractors) {
                policies.addAll(policyExtractor.extractRowLevelPolicies(method));
            }
        }
        return policies;
    }
}
