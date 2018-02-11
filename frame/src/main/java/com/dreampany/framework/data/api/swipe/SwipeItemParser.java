package com.dreampany.framework.data.api.swipe;

import android.content.Context;
import android.content.res.XmlResourceParser;
import android.support.annotation.XmlRes;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

final class SwipeItemParser {
    private final Context context;
    private final XmlResourceParser parser;
    private final List<SwipeItem> swipeItems;

    private int currentEventType;
    private SwipeItem currentlyProcessedItem;

    SwipeItemParser(Context context, @XmlRes int swipeItemsRes) {
        this.context = context;

        parser = context.getResources().getXml(swipeItemsRes);
        swipeItems = new ArrayList<>();
    }

    List<SwipeItem> parseItems() {
        try {
            parser.next();
            currentEventType = parser.getEventType();

            while (!isAtDocumentEnd()) {
                if (isAtStartOfNewItem()) {
                    processNewItem();
                } else if (isAtEndOfAnItem()) {
                    finishProcessingItem();
                }

                currentEventType = parser.next();
            }
        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
            throw new SwipeItemParserException();
        }

        return swipeItems;
    }

    private boolean isAtDocumentEnd() {
        return currentEventType == XmlResourceParser.END_DOCUMENT;
    }

    private boolean isAtStartOfNewItem() {
        return currentEventType == XmlResourceParser.START_TAG
                && "item".equals(parser.getName());
    }

    private void processNewItem() {
        if (currentlyProcessedItem == null) {
            currentlyProcessedItem = new SwipeItem();
        }

        for (int i = 0; i < parser.getAttributeCount(); i++) {
            populateAttributeAtPosition(i);
        }
    }

    private void populateAttributeAtPosition(int position) {
        String attributeName = parser.getAttributeName(position);

        switch (attributeName) {
            case "value":
                String value = getStringValue(position);
                currentlyProcessedItem.setValue(value);
                break;
            case "title":
                String title = getStringValue(position);
                currentlyProcessedItem.setTitle(title);
                break;
            case "description":
                String description = getStringValue(position);
                currentlyProcessedItem.setDescription(description);
                break;
        }
    }

    private String getStringValue(int position) {
        int stringResource = parser.getAttributeResourceValue(position, 0);

        if (stringResource != 0) {
            return context.getString(stringResource);
        }

        return parser.getAttributeValue(position);
    }

    private boolean isAtEndOfAnItem() {
        return currentEventType == XmlResourceParser.END_TAG
                && "item".equals(parser.getName())
                && currentlyProcessedItem != null;
    }

    private void finishProcessingItem() {
        swipeItems.add(currentlyProcessedItem);
        currentlyProcessedItem = null;
    }

    private class SwipeItemParserException extends RuntimeException {
    }
}