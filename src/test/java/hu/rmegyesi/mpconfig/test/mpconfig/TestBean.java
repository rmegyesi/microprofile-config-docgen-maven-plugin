package hu.rmegyesi.mpconfig.test.mpconfig;

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
import org.eclipse.microprofile.config.inject.ConfigProperty;

public class TestBean {

    @ExpectedProperties({"my-config"})
    @ConfigProperty(name = "my-config")
    String namedStringInBean;

    @ExpectedProperties({"hu.rmegyesi.mpconfig.test.mpconfig.TestBean.unnamedStringInBean"})
    @ConfigProperty
    String unnamedStringInBean;

}
