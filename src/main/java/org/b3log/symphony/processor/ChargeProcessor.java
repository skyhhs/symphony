/*
 * Symphony - A modern community (forum/BBS/SNS/blog) platform written in Java.
 * Copyright (C) 2012-present, b3log.org
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package org.b3log.symphony.processor;

import org.b3log.latke.http.HttpMethod;
import org.b3log.latke.http.RequestContext;
import org.b3log.latke.http.annotation.After;
import org.b3log.latke.http.annotation.Before;
import org.b3log.latke.http.annotation.RequestProcessing;
import org.b3log.latke.http.annotation.RequestProcessor;
import org.b3log.latke.http.renderer.AbstractFreeMarkerRenderer;
import org.b3log.latke.ioc.Inject;
import org.b3log.symphony.processor.middleware.AnonymousViewCheckMidware;
import org.b3log.symphony.processor.middleware.stopwatch.StopwatchEndAdvice;
import org.b3log.symphony.processor.middleware.stopwatch.StopwatchStartAdvice;
import org.b3log.symphony.service.DataModelService;

import java.util.Map;

/**
 * Charge processor.
 * <ul>
 * <li>Shows point charge (/charge/point), GET</li>
 * </ul>
 *
 * @author <a href="http://88250.b3log.org">Liang Ding</a>
 * @version 1.1.0.2, Oct 26, 2016
 * @since 1.3.0
 */
@RequestProcessor
public class ChargeProcessor {

    /**
     * Data model service.
     */
    @Inject
    private DataModelService dataModelService;

    /**
     * Shows charge point.
     *
     * @param context the specified context
     */
    @RequestProcessing(value = "/charge/point", method = HttpMethod.GET)
    @Before({StopwatchStartAdvice.class, AnonymousViewCheckMidware.class})
    @After({PermissionGrant.class, StopwatchEndAdvice.class})
    public void showChargePoint(final RequestContext context) {
        final AbstractFreeMarkerRenderer renderer = new SkinRenderer(context, "charge-point.ftl");
        final Map<String, Object> dataModel = renderer.getDataModel();
        dataModelService.fillHeaderAndFooter(context, dataModel);
        dataModelService.fillRandomArticles(dataModel);
        dataModelService.fillSideHotArticles(dataModel);
        dataModelService.fillSideTags(dataModel);
        dataModelService.fillLatestCmts(dataModel);
    }
}
