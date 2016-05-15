/*
 * Copyright 2016 LINE Corporation
 *
 * LINE Corporation licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package com.linecorp.bot.client.rich;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;

import java.util.Collection;

import org.junit.Test;

import com.linecorp.bot.model.rich.RichMessage;
import com.linecorp.bot.model.rich.RichMessageCanvas;
import com.linecorp.bot.model.rich.RichMessageImage;
import com.linecorp.bot.model.rich.RichMessageScene;
import com.linecorp.bot.model.rich.RichMessageSceneImage;
import com.linecorp.bot.model.rich.RichMessageSceneListener;
import com.linecorp.bot.model.rich.action.AbstractRichMessageAction;
import com.linecorp.bot.model.rich.action.SendMessageRichMessageAction;
import com.linecorp.bot.model.rich.action.WebRichMessageAction;

public class SimpleRichMessageBuilderTest {

    @Test
    public void testBuildingSimpleRichMessage() throws Exception {
        final RichMessage richMessage =
                SimpleRichMessageBuilder.create(1040, 520)
                                        .addWebAction(0, 0, 520, 520, "altTxt", "https://example.com")
                                        .addSendMessageAction(520, 0, 520, 520, "hello")
                                        .build();

        assertThat(richMessage.getCanvas(), is(new RichMessageCanvas("scene1", 1040, 520)));
        assertThat(richMessage.getImages().values(), contains(new RichMessageImage(0, 0, 1040, 520)));

        final Collection<AbstractRichMessageAction> actions = richMessage.getActions().values();
        assertThat(actions, contains(new WebRichMessageAction("altTxt", "https://example.com"),
                                     new SendMessageRichMessageAction("hello")));

        final RichMessageScene scene = richMessage.getScenes().get("scene1");
        assertThat(scene.getDraws(), contains(new RichMessageSceneImage("image1", 0, 0, 1040, 520)));
        assertThat(scene.getListeners(), contains(new RichMessageSceneListener("action1", 0, 0, 520, 520),
                                                  new RichMessageSceneListener("action2", 520, 0, 520, 520)));
    }

}
