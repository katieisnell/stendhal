/***************************************************************************
 *                   (C) Copyright 2003-2015 - Stendhal                    *
 ***************************************************************************
 ***************************************************************************
 *                                                                         *
 *   This program is free software; you can redistribute it and/or modify  *
 *   it under the terms of the GNU General Public License as published by  *
 *   the Free Software Foundation; either version 2 of the License, or     *
 *   (at your option) any later version.                                   *
 *                                                                         *
 ***************************************************************************/
package games.stendhal.client.actions;

import static org.hamcrest.core.StringContains.containsString;
import static org.junit.Assert.assertThat;

import org.junit.BeforeClass;
import org.junit.Test;

public class SlashActionRepositoryTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		SlashActionRepository.register();
	}

	/**
	 * Tests for get.
	 */
	@Test
	public void testGet() {
		assertThat(SlashActionRepository.get("alter").toString(), containsString("AlterAction"));
		assertThat(SlashActionRepository.get("/").toString(), containsString("RemessageAction"));
		assertThat(SlashActionRepository.get("add").toString(), containsString("AddBuddyAction"));
		assertThat(SlashActionRepository.get("adminlevel").toString(), containsString("AdminLevelAction"));
		assertThat(SlashActionRepository.get("alter").toString(), containsString("AlterAction"));
		assertThat(SlashActionRepository.get("altercreature").toString(), containsString("AlterCreatureAction"));
		assertThat(SlashActionRepository.get("alterquest").toString(), containsString("AlterQuestAction"));
		assertThat(SlashActionRepository.get("answer").toString(), containsString("AnswerAction"));
		assertThat(SlashActionRepository.get("away").toString(), containsString("DefaultAction"));
		assertThat(SlashActionRepository.get("drop").toString(), containsString("DropAction"));
		assertThat(SlashActionRepository.get("gag").toString(), containsString("DefaultAction"));
		assertThat(SlashActionRepository.get("gmhelp").toString(), containsString("GMHelpAction"));
		assertThat(SlashActionRepository.get("grumpy").toString(), containsString("DefaultAction"));
		assertThat(SlashActionRepository.get("help").toString(), containsString("HelpAction"));
		assertThat(SlashActionRepository.get("ignore").toString(), containsString("IgnoreAction"));
		assertThat(SlashActionRepository.get("inspect").toString(), containsString("DefaultAction"));
		assertThat(SlashActionRepository.get("invisible").toString(), containsString("DefaultAction"));
		assertThat(SlashActionRepository.get("jail").toString(), containsString("DefaultAction"));
		assertThat(SlashActionRepository.get("msg").toString(), containsString("DefaultAction"));
		assertThat(SlashActionRepository.get("quit").toString(), containsString("QuitAction"));
		assertThat(SlashActionRepository.get("remove").toString(), containsString("DefaultAction"));
		assertThat(SlashActionRepository.get("sound").toString(), containsString("SoundAction"));
		assertThat(SlashActionRepository.get("summonat").toString(), containsString("SummonAtAction"));
		assertThat(SlashActionRepository.get("summon").toString(), containsString("SummonAction"));
		assertThat(SlashActionRepository.get("supportanswer").toString(), containsString("DefaultAction"));
		assertThat(SlashActionRepository.get("supporta").toString(), containsString("DefaultAction"));
		assertThat(SlashActionRepository.get("support").toString(), containsString("DefaultAction"));
		assertThat(SlashActionRepository.get("teleport").toString(), containsString("DefaultAction"));
		assertThat(SlashActionRepository.get("teleportto").toString(), containsString("DefaultAction"));
		assertThat(SlashActionRepository.get("tellall").toString(), containsString("DefaultAction"));
		assertThat(SlashActionRepository.get("tell").toString(), containsString("DefaultAction"));
		assertThat(SlashActionRepository.get("where").toString(), containsString("DefaultAction"));
		assertThat(SlashActionRepository.get("who").toString(), containsString("DefaultAction"));
	}

}
