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

import static org.testng.Assert.assertEquals;
import static org.testng.AssertJUnit.assertNotNull;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.peergreen.deployment.Artifact;
import com.peergreen.deployment.ArtifactBuilder;
import com.peergreen.deployment.internal.artifact.ImmutableArtifactBuilder;
import com.peergreen.deployment.urls.UrlsDeploymentPlan;

/**
 * @author Mohammed Boukada
 */
public class BaseUrlsDeploymentPlanParserTestCase {

    private ArtifactBuilder artifactBuilder;

    private InputStream getIS(String path) {
        return getClass().getResourceAsStream(path);
    }

    @BeforeClass
    public void init() {
        artifactBuilder = new ImmutableArtifactBuilder();
    }

    @Test
    public void testNormalParsing() throws IOException, URISyntaxException {
        UrlsDeploymentPlanParser parser = new BaseUrlsDeploymentPlanParser(artifactBuilder);
        UrlsDeploymentPlan deploymentPlan = parser.parse(getIS("/test-urls-deployment-plan-parsing.urls"));

        assertNotNull(deploymentPlan);
        assertEquals(deploymentPlan.getArtifacts().size(), 1);
        URI uri = new URI("mvn:com.peergreen.example.paxexam/paxexam-hello-service/1.0.0");
        Artifact expected = artifactBuilder.build("mvn:com.peergreen.example.paxexam/paxexam-hello-service/1.0.0", uri);
        Artifact found = deploymentPlan.getArtifacts().iterator().next();
        assertEquals(found.name(), expected.name());
        assertEquals(found.uri(), expected.uri());
    }
}
