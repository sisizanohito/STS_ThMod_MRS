package ThMod_FnH.cards.Marisa;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import basemod.abstracts.CustomCard;
import ThMod_FnH.action.UltraShortWaveAction;
import ThMod_FnH.patches.AbstractCardEnum;
import ThMod_FnH.powers.Marisa.ChargeUpPower;

public class UltraShortWave extends CustomCard {
	public static final String ID = "UltraShortWave";
	public static final String IMG_PATH = "img/cards/Defend.png";
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;
	private static final int COST = 1;
	private static final int ENGY_GAIN = 1;
	


	public UltraShortWave() {
		super(ID, NAME, IMG_PATH, COST, DESCRIPTION, AbstractCard.CardType.SKILL,
				AbstractCardEnum.MARISA_COLOR, AbstractCard.CardRarity.UNCOMMON, AbstractCard.CardTarget.SELF);

		this.baseMagicNumber = this.magicNumber = ENGY_GAIN;
		this.block = this.baseBlock = this.magicNumber+1;
	}
	
	public void use(AbstractPlayer p, AbstractMonster m) {
		AbstractDungeon.actionManager.addToBottom(new GainEnergyAction(this.magicNumber));
		AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p,p,new ChargeUpPower(p,this.magicNumber+1),this.magicNumber+1));
		AbstractDungeon.actionManager.addToBottom(new UltraShortWaveAction());
		this.upgradeMagicNumber(1);
		this.block = this.baseBlock = this.magicNumber+1;
	}

	public AbstractCard makeCopy() {
		return new UltraShortWave();
	}

	public void upgrade() {
		if (!this.upgraded) {
			upgradeName();
			upgradeBaseCost(0);
		}
	}
}