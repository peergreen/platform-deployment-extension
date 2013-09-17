/**
 * Copyright 2013 Peergreen S.A.S. All rights reserved.
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.peergreen.deployment.urls.processor.parser;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;

import com.peergreen.deployment.urls.UrlsDeploymentPlan;

/**
 * @author Mohammed Boukada
 */
public interface UrlsDeploymentPlanParser {

    UrlsDeploymentPlan parse(InputStream is) throws IOException, URISyntaxException;
}
