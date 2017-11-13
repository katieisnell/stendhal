/* $Id$ */
// Quest where Lon Jatham asks Java questions as a quest and
// gives XP and a certificate for answering them correctly.

package games.stendhal.server.maps.quests;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import games.stendhal.common.Rand;
import games.stendhal.common.parser.ConversationParser;
import games.stendhal.server.entity.npc.action.EquipItemAction;
import games.stendhal.common.parser.Expression;
import games.stendhal.common.parser.JokerExprMatcher;
import games.stendhal.common.parser.Sentence;
import games.stendhal.common.parser.SimilarExprMatcher;
import games.stendhal.server.entity.npc.ChatAction;
import games.stendhal.server.entity.npc.ConversationPhrases;
import games.stendhal.server.entity.npc.ConversationStates;
import games.stendhal.server.entity.npc.EventRaiser;
import games.stendhal.server.entity.npc.SpeakerNPC;
import games.stendhal.server.entity.npc.condition.GreetingMatchesNameCondition;
import games.stendhal.server.entity.npc.condition.QuestActiveCondition;
import games.stendhal.server.entity.npc.condition.QuestCompletedCondition;
import games.stendhal.server.entity.npc.condition.QuestNotStartedCondition;
import games.stendhal.server.entity.player.Player;
import games.stendhal.server.maps.Region;


// Java quest is an abstract quest
public class JavaQuiz extends AbstractQuest {
    // Quest slot name
	private static final String QUEST_SLOT = "java_quest";

	// The questions and answers
	private static Map<String, String> answers = new HashMap<String, String>();
	static {
			answers.put("Do you attend all my lectures?", "yes");
			answers.put("An object is an instance of a ...",
							"class");
			answers.put("What result would Java give for 1/0.0?",
							"Infty");
	}

        // Put in slot name
	@Override
	public String getSlotName() {
		return QUEST_SLOT;
	}
	
	// get history of player and quest
	@Override
	public List<String> getHistory(final Player player) {
		final List<String> res = new ArrayList<String>();

		res.add("I met a Java lecturer! If I can pass his exam I can get some XP and a certificate!");
		if (!player.isQuestCompleted(QUEST_SLOT)) {
			res.add("The question I must answer is " + player.getQuest(QUEST_SLOT) + ".");
		} else {
			res.add("I passed Lon's exam and got the certificate.");
		}
		return res;
	}

	// See if player can get their certificate and XP from answering a question.
	private void createCertificate() {
		final SpeakerNPC lon = npcs.get("Lon Jatham");

		lon.add(ConversationStates.IDLE,
			ConversationPhrases.GREETING_MESSAGES,
			new GreetingMatchesNameCondition(lon.getName()), true,
			ConversationStates.ATTENDING, null,
			new ChatAction() {
				@Override
				public void fire(final Player player, final Sentence sentence, final EventRaiser npc) {
					if (!player.hasQuest(QUEST_SLOT)) {
						npc.say("I am a Java lecturer who's stuck in his ways!");
					} else if (!player.isQuestCompleted(QUEST_SLOT)) {
						final String name = player.getQuest(QUEST_SLOT);
						 npc.say("You're back! I trust you read my book. " + name);
						npc.setCurrentState(ConversationStates.QUESTION_1);
					} else {
						 npc.say("Congratulations!");
					}
				}
			});

		lon.add(ConversationStates.ATTENDING,
				ConversationPhrases.combine(ConversationPhrases.QUEST_MESSAGES, Arrays.asList("exam", "test")),
				new QuestNotStartedCondition(QUEST_SLOT),
				ConversationStates.QUEST_OFFERED,
				"Are you ready to take the test?",
				null);

		lon.add(ConversationStates.ATTENDING,
				ConversationPhrases.combine(ConversationPhrases.QUEST_MESSAGES, Arrays.asList("exam", "test")),
				new QuestCompletedCondition(QUEST_SLOT),
				ConversationStates.ATTENDING,
				 "You've already taken the test! Now go ahead and code!!!",
				null);

		lon.add(ConversationStates.ATTENDING,
				ConversationPhrases.combine(ConversationPhrases.QUEST_MESSAGES, Arrays.asList("exam", "test")),
				new QuestActiveCondition(QUEST_SLOT),
				ConversationStates.QUESTION_1,
				null,
				new ChatAction() {
					@Override
					public void fire(final Player player, final Sentence sentence, final EventRaiser npc) {
						final String name = player.getQuest(QUEST_SLOT);
						npc.say("I trust you studied up and can answer the question. " + name);
					}
				});

		lon.add(ConversationStates.QUEST_OFFERED,
			ConversationPhrases.NO_MESSAGES, null,
			ConversationStates.ATTENDING,
			 "Why wouldn't you want to take the test? Haven't you read my book?", null);

		lon.add(ConversationStates.QUEST_OFFERED,
			ConversationPhrases.YES_MESSAGES, null,
			ConversationStates.QUESTION_1, null,
			new ChatAction() {
				@Override
				public void fire(final Player player, final Sentence sentence, final EventRaiser npc) {
					final String name = Rand.rand(answers.keySet());
					npc.say("Very well. Here is your question. " + name);
					player.setQuest(QUEST_SLOT, name);
				}
			});

	

		lon.addMatching(ConversationStates.QUESTION_1, Expression.JOKER, new JokerExprMatcher(), null,
			ConversationStates.ATTENDING, null,
			new ChatAction() {
				@Override
				public void fire(final Player player, final Sentence sentence, final EventRaiser npc) {
					final String name = player.getQuest(QUEST_SLOT);
					final String quote = answers.get(name);

					final Sentence answer = sentence.parseAsMatchingSource();
					final Sentence expected = ConversationParser.parse(quote, new SimilarExprMatcher());

					if (answer.matchesFull(expected)) {
						npc.say("Correct, well done! You now get some XP and a certificate!");
						 player.addXP(100);
						new EquipItemAction("certificate", 1, true).fire(player, sentence, npc);
						 
						player.setQuest(QUEST_SLOT, "done");
						player.notifyWorldAboutChanges();

					} else if (ConversationPhrases.GOODBYE_MESSAGES.contains(sentence.getTriggerExpression().getNormalized())) {
						npc.say("Good bye - see you next time!");
						npc.setCurrentState(ConversationStates.IDLE);
					} else {
						npc.setCurrentState(ConversationStates.ATTENDING);
						npc.say("Incorrect. #Study up and come back to me.");
					}
				}
			});
	}

	// Add the quest to the world
	@Override
	public void addToWorld() {
		fillQuestInfo(
				"Java Quiz",
				"Lon gives XP and a certificate for passing his Java exam.",
				false);
		createCertificate();
	}
	
	// Quest name
	@Override
	public String getName() {
		return "JavaQuiz";
	}

	// Region name
	@Override
	public String getRegion() {
		return Region.KALAVAN;
	}
	@Override
	
	// NPC name
	public String getNPCName() {
		return "Lon Jatham";
	}
}