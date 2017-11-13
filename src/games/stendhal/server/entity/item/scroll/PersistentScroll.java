package games.stendhal.server.entity.item.scroll;

/* $Id$
/**************************************************************************
*                   (C) Copyright 2003-2010 - Stendhal                    *
***************************************************************************
***************************************************************************
*                                                                         *
*   This program is free software; you can redistribute it and/or modify  *
*   it under the terms of the GNU General Public License as published by  *
*   the Free Software Foundation; either version 2 of the License, or     *
*   (at your option) any later version.                                   *
*                                                                         *
***************************************************************************/

import java.util.Map;

import org.apache.log4j.Logger;

import games.stendhal.server.entity.Entity;
import games.stendhal.server.entity.RPEntity;
import games.stendhal.server.entity.item.StackableItem;
import games.stendhal.server.entity.player.Player;
import marauroa.common.game.RPObject;

/**
 * Represents a scroll that does not disappear from the inventory when used
 */
public class PersistentScroll extends StackableItem {

	private static final Logger logger = Logger.getLogger(PersistentScroll.class);

	/**
	 * Creates a new scroll.
	 *
	 * @param name
	 * @param clazz
	 * @param subclass
	 * @param attributes
	 */
	public PersistentScroll(final String name, final String clazz, final String subclass,
			final Map<String, String> attributes) {
		super(name, clazz, subclass, attributes);
	}

	/**
	 * Copy constructor.
	 *
	 * @param item
	 *            item to copy
	 */
	public PersistentScroll(final PersistentScroll item) {
		super(item);
	}

	@Override
	public final boolean onUsed(final RPEntity user) {
		RPObject base = getBaseContainer();

		if (user.nextTo((Entity) base)) {

			if (user instanceof Player && useScroll((Player)user)) {
				user.notifyWorldAboutChanges();
				return true;
			} else {
				return false;
			}
		} else {
			logger.debug("Scroll is too far away.");
			return false;
		}
	}

	/**
	 * Use a scroll.
	 *
	 * @param player
	 *            The player using scroll.
	 *
	 * @return <code>true</code> if successful, <code>false</code>
	 *         otherwise.
	 */
	protected boolean useScroll(final Player player) {
		player.sendPrivateText("What a strange scroll! You can't make heads or tails of it.");
		return false;
	}

}
