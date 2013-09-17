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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Provides;
import org.apache.felix.ipojo.annotations.Requires;

import com.peergreen.deployment.Artifact;
import com.peergreen.deployment.ArtifactBuilder;
import com.peergreen.deployment.urls.UrlsDeploymentPlan;

/**
 * @author Mohammed Boukada
 */
@Component
@Instantiate
@Provides
public class BaseUrlsDeploymentPlanParser implements UrlsDeploymentPlanParser {

    private static final String HASH = "#";
    private static final String DOUBLE_SLASH = "//";

    private ArtifactBuilder artifactBuilder;

    public BaseUrlsDeploymentPlanParser(@Requires ArtifactBuilder artifactBuilder) {
        this.artifactBuilder = artifactBuilder;
    }

    @Override
    public UrlsDeploymentPlan parse(InputStream is) throws IOException, URISyntaxException {
        UrlsDeploymentPlan deploymentPlan = new UrlsDeploymentPlan();

        BufferedReader br = new BufferedReader(new InputStreamReader(is));

        String line;
        while ((line = br.readLine()) != null) {
            String uri = line.trim();
            if (!("".equals(uri) || uri.startsWith(HASH) || uri.startsWith(DOUBLE_SLASH))) {
                Artifact artifact = artifactBuilder.build(uri, new URI(uri));
                deploymentPlan.add(artifact);
            }
        }
        br.close();
        return deploymentPlan;
    }
}
