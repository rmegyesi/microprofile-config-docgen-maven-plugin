package hu.rmegyesi.mpconfig.test.smallryeconfig;

/*-
 * #%L
 * Microprofile Config Docgen Maven Plugin
 * %%
 * Copyright (C) 2024 Róbert Megyesi
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import hu.rmegyesi.mpconfig.docgen.ExpectedProperties;
import io.smallrye.config.ConfigMapping;
import io.smallrye.config.WithName;
import io.smallrye.config.WithParentName;

@ConfigMapping(prefix = "nested")
public interface NestedConfigMapping {

    interface OneLevel {
        String oneLevelStringAttribute();
    }

    @ExpectedProperties({"nested.one-level.one-level-string-attribute"})
    OneLevel oneLevel();

    @ExpectedProperties({"nested.my-first-level.one-level-string-attribute"})
    @WithName("my-first-level")
    OneLevel oneLevelNamed();

    interface MultiLevelOne {
        String multiLevelOneString();

        MultiLevelTwo multiLevelTwo();

        interface NestedInterface {
            String nestedInterfaceProp();
        }

        NestedInterface nestedInterface();
    }

    interface MultiLevelTwo {
        String multiLevelTwoString();
    }


    @ExpectedProperties({
            "nested.multi-level-one.multi-level-one-string",
            "nested.multi-level-one.multi-level-two.multi-level-two-string",
            "nested.multi-level-one.nested-interface.nested-interface-prop"
    })
    MultiLevelOne multiLevelOne();

    @ExpectedProperties({
            "nested.my-multi-level.multi-level-one-string",
            "nested.my-multi-level.multi-level-two.multi-level-two-string",
            "nested.my-multi-level.nested-interface.nested-interface-prop"
    })
    @WithName("my-multi-level")
    MultiLevelOne multiLevelOneNamed();

    @ExpectedProperties({
            "nested.multi-level-one-string",
            "nested.multi-level-two.multi-level-two-string",
            "nested.nested-interface.nested-interface-prop"
    })
    @WithParentName
    MultiLevelOne multiLevelOneWithParentName();
}
