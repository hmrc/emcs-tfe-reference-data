/*
 * Copyright 2023 HM Revenue & Customs
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

package uk.gov.hmrc.emcstfereferencedata.utils

import uk.gov.hmrc.emcstfereferencedata.support.UnitSpec

class StringUtilsSpec extends UnitSpec {
  "removeHtmlEscapedCharactersAndAddSmartQuotes" should {
    "convert a String with HTML escaped characters" in {
      StringUtils.removeHtmlEscapedCharactersAndAddSmartQuotes("bacon &amp; eggs") shouldBe "bacon & eggs"
    }

    "convert a String with single quotes" in {
      StringUtils.removeHtmlEscapedCharactersAndAddSmartQuotes("it's time for 'beans'") shouldBe "it’s time for ‘beans’"
    }

    "convert a String with both HTML escaped characters and single quotes" in {
      StringUtils.removeHtmlEscapedCharactersAndAddSmartQuotes("This is a &lsquo;test', it's a good 'test&rsquo; and it will be 'tested'") shouldBe
        "This is a ‘test’, it’s a good ‘test’ and it will be ‘tested’"
    }
  }
}
