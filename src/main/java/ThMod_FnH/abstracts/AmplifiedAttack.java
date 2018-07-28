package ThMod_FnH.abstracts;

import java.util.ArrayList;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

import ThMod_FnH.ThMod;
import basemod.abstracts.CustomCard;

public abstract class AmplifiedAttack extends CustomCard  {
	private int ampDamage = -1;
	public int ampNumber = 0;
	public int[] multiAmpDamage;

	public AmplifiedAttack(String id, String name, String img, int cost, String rawDescription, CardType type,
			CardColor color, CardRarity rarity, CardTarget target) {
		super(id, name, img, cost, rawDescription, type, color, rarity, target);
		// TODO Auto-generated constructor stub
	}


	public abstract void upgrade();

	public abstract void use(AbstractPlayer arg0, AbstractMonster arg1);

	public abstract AbstractCard makeCopy();

	@Override
	public void applyPowers(){
		AbstractPlayer player = AbstractDungeon.player;
		
		switch (this.cardID) {
		case "MeteonicShower":
			this.damage = this.baseDamage;
			this.block = this.baseBlock;
			break;
		case "LuminesStrike":
			break;
		default:
			this.damage = this.baseDamage;
			this.ampDamage = this.baseDamage + this.ampNumber;
			this.block = this.baseBlock = this.ampDamage;
		}
		
		this.isDamageModified = false;
		this.isBlockModified = false;
		
		ThMod.logger.info(
				"AmplifiedAttack :"+this.cardID+ ": applyPowers : baseDamage :"+
				this.baseDamage +"; damage :"+this.damage+"; ampNumber : "+this.ampNumber+
				"; ampDamage :"+this.ampDamage+"; baseBlock :"+this.baseBlock+"; block :"+this.block);
		
		if (!this.isMultiDamage){
			float tmp = this.damage;
			float amp = this.block;
			if ((AbstractDungeon.player.hasRelic("WristBlade")) && (this.costForTurn == 0)){
				tmp += 3.0F;
				if (this.baseDamage != (int)tmp) {
					this.isDamageModified = true;
				}
			}
			for (AbstractPower p : player.powers){
				tmp = p.atDamageGive(tmp, this.damageTypeForTurn);
				amp = p.atDamageGive(amp, this.damageTypeForTurn);
				if (this.baseDamage != (int)tmp){
					this.isDamageModified = true;
				}
				if (this.ampDamage != (int)amp){
					this.isBlockModified = true;
				}
			}
			for (AbstractPower p : player.powers){
				tmp = p.atDamageFinalGive(tmp, this.damageTypeForTurn);
				amp = p.atDamageFinalGive(amp, this.damageTypeForTurn);
				if (this.baseDamage != (int)tmp){
					this.isDamageModified = true;
				}
				if (this.ampDamage != (int)amp){
					this.isBlockModified = true;
				}
			}
			if (tmp < 0.0F) {
				tmp = 0.0F;
			}
			if (amp < 0.0F) {
				amp = 0.0F;
			}
			this.damage = MathUtils.floor(tmp);
			this.block = MathUtils.floor(amp);
			
			ThMod.logger.info(
					"AmplifiedAttack :"+this.cardID+": done applyPowers : baseDamage :"+
					this.baseDamage+"; damage :"+this.damage+"; ampNumber : "+this.ampNumber+
					"; ampDamage :"+this.ampDamage+"; baseBlock :"+this.baseBlock+"; block :"+this.block);
			
		}
		else{
			ArrayList<AbstractMonster> m = AbstractDungeon.getCurrRoom().monsters.monsters;
			float[] tmp = new float[m.size()];
			float[] amp = new float[m.size()];
			for (int i = 0; i < tmp.length; i++){
				tmp[i] = this.damage;
			}
			for (int i = 0; i < tmp.length; i++){
				amp[i] = this.ampDamage;
			}
			for (int i = 0; i < tmp.length; i++){
				if ((AbstractDungeon.player.hasRelic("WristBlade")) && (this.cost == 0)){
					tmp[i] += 3.0F;
					amp[i] += 3.0F;
					if (this.baseDamage != (int)tmp[i]){
						this.isDamageModified = true;
					}
					if (this.ampDamage != (int)amp[i]){
						this.isBlockModified = true;
					}
				}
				for (AbstractPower p : player.powers){
					tmp[i] = p.atDamageGive(tmp[i], this.damageTypeForTurn);
					amp[i] = p.atDamageGive(amp[i], this.damageTypeForTurn);
					if (this.baseDamage != (int)tmp[i]){
						this.isDamageModified = true;
					}
					if (this.ampDamage != (int)amp[i]){
						this.isBlockModified = true;
					}
				}
			}
			for (int i = 0; i < tmp.length; i++) {
				for (AbstractPower p : player.powers){
					tmp[i] = p.atDamageFinalGive(tmp[i], this.damageTypeForTurn);
					amp[i] = p.atDamageFinalGive(amp[i], this.damageTypeForTurn);
					if (this.baseDamage != (int)tmp[i]) {
						this.isDamageModified = true;
					}
					if (this.ampDamage != (int)amp[i]){
						this.isBlockModified = true;
					}
				}
			}
			for (int i = 0; i < tmp.length; i++) {
				if (tmp[i] < 0.0F) {
					tmp[i] = 0.0F;
				}
				if (amp[i] < 0.0F) {
					amp[i] = 0.0F;
				}
			}
			this.multiDamage = new int[tmp.length];
			for (int i = 0; i < tmp.length; i++) {
				this.multiDamage[i] = MathUtils.floor(tmp[i]);
			}
			this.multiAmpDamage = new int[amp.length];
			for (int i = 0; i < amp.length; i++) {
				this.multiAmpDamage[i] = MathUtils.floor(amp[i]);
			}
			this.damage = this.multiDamage[0];
			this.block = this.multiAmpDamage[0];
			/*
			ThMod.logger.info("AmplifiedAttack : done applyPowers (multiple) : baseDamage :"+this.baseDamage +"; damage :"+this.damage+"; ampNumber : "+this.ampNumber+
					"; ampDamage :"+this.ampDamage+"; block :"+this.block);
			ThMod.logger.info("AmplifiedAttack : done applyPowers (multiple) : multiDamage[0] :"+this.multiDamage[0] +
					"; multiAmpDamage[0] :"+this.multiAmpDamage[0]);
			*/
		}
	}

	@Override
	public void calculateDamageDisplay(AbstractMonster mo){
		calculateCardDamage(mo);
	}

	@Override
	public void calculateCardDamage(AbstractMonster mo){
		AbstractPlayer player = AbstractDungeon.player;

		switch (this.cardID) {
		case "MeteonicShower":
			this.damage = this.baseDamage;
			this.block = this.baseBlock;
			break;
		case "LuminesStrike":
			break;
		default:
			this.damage = this.baseDamage;
			this.ampDamage = this.baseDamage + this.ampNumber;
			this.block = this.baseBlock = this.ampDamage;
		}
		
		this.isDamageModified = false;
		this.isBlockModified = false;
	
		ThMod.logger.info("AmplifiedAttack : calculateCardDamage : baseDamage :"+this.baseDamage +"; damage :"+this.damage+"; ampNumber : "+this.ampNumber+
				"; ampDamage :"+this.ampDamage+"; baseBlock :"+this.baseBlock+"; block :"+this.block);
		
		if ((!this.isMultiDamage) && (mo != null)){
			float tmp = this.damage;
			float amp = this.block;
			
			if ((AbstractDungeon.player.hasRelic("WristBlade")) && (this.costForTurn == 0)){
				tmp += 3.0F;
				amp += 3.0F;
				if (this.baseDamage != (int)tmp){
					this.isDamageModified = true;
				}
				if (this.ampDamage != (int)amp){
					this.isBlockModified = true;
				}
				
			}
			for (AbstractPower p : player.powers){
				tmp = p.atDamageGive(tmp, this.damageTypeForTurn);
				amp = p.atDamageGive(amp, this.damageTypeForTurn);
				if (this.baseDamage != (int)tmp){
					this.isDamageModified = true;
				}
				if (this.ampDamage != (int)amp){
					this.isBlockModified = true;
				}
			}
			if (mo != null){
				for (AbstractPower p : mo.powers){
					tmp = p.atDamageReceive(tmp, this.damageTypeForTurn);
					amp = p.atDamageReceive(amp, this.damageTypeForTurn);
				}
			}
			
			for (AbstractPower p : player.powers){
				tmp = p.atDamageFinalGive(tmp, this.damageTypeForTurn);
				amp = p.atDamageFinalGive(amp, this.damageTypeForTurn);
				if (this.baseDamage != (int)tmp){
					this.isDamageModified = true;
				}
				if (this.ampDamage != (int)amp){
					this.isBlockModified = true;
				}
			}
			if (mo != null){
				for (AbstractPower p : mo.powers){
					tmp = p.atDamageFinalReceive(tmp, this.damageTypeForTurn);
					amp = p.atDamageFinalReceive(amp, this.damageTypeForTurn);
					if (this.baseDamage != (int)tmp){
						this.isDamageModified = true;
					}
					if (this.ampDamage != (int)amp){
						this.isBlockModified = true;
					}
				}
			}
			if (tmp < 0.0F) {
		        tmp = 0.0F;
			}
			if (amp < 0.0F) {
				amp = 0.0F;
			}
			this.damage = MathUtils.floor(tmp);
			this.block = MathUtils.floor(amp);
			
			ThMod.logger.info("AmplifiedAttack : done calculateCardDamage : baseDamage :"+this.baseDamage +"; damage :"+this.damage+"; ampNumber : "+this.ampNumber+
					"; ampDamage :"+this.ampDamage+"; baseBlock :"+this.baseBlock+"; block :"+this.block);
			
		}else{
			ArrayList<AbstractMonster> m = AbstractDungeon.getCurrRoom().monsters.monsters;
			float[] tmp = new float[m.size()];
			float[] amp = new float[m.size()];
			for (int i = 0; i < tmp.length; i++){
				tmp[i] = this.damage;
			}
			for (int i = 0; i < tmp.length; i++){
				amp[i] = this.ampDamage;
			}
			for (int i = 0; i < tmp.length; i++){
				if ((AbstractDungeon.player.hasRelic("WristBlade")) && (this.cost == 0)){
					tmp[i] += 3.0F;
					amp[i] += 3.0F;
					if (this.baseDamage != (int)tmp[i]){
						this.isDamageModified = true;
					}
					if (this.ampDamage != (int)amp[i]){
						this.isBlockModified = true;
					}
				}
				for (AbstractPower p : player.powers){
					tmp[i] = p.atDamageGive(tmp[i], this.damageTypeForTurn);
					amp[i] = p.atDamageGive(amp[i], this.damageTypeForTurn);
					if (this.baseDamage != (int)tmp[i]){
						this.isDamageModified = true;
					}
					if (this.ampDamage != (int)amp[i]){
						this.isBlockModified = true;
					}
				}
			}
			for (int i = 0; i < tmp.length; i++) {
				for (AbstractPower p : ((AbstractMonster)m.get(i)).powers) {
					if ((!((AbstractMonster)m.get(i)).isDying) && (!((AbstractMonster)m.get(i)).isEscaping)) {
						tmp[i] = p.atDamageReceive(tmp[i], this.damageTypeForTurn);
						amp[i] = p.atDamageReceive(amp[i], this.damageTypeForTurn);
					}
				}
			}
			for (int i = 0; i < tmp.length; i++) {
	    	  for (AbstractPower p : player.powers){
	    		  tmp[i] = p.atDamageFinalGive(tmp[i], this.damageTypeForTurn);
	    		  amp[i] = p.atDamageFinalGive(amp[i], this.damageTypeForTurn);
	    		  if (this.baseDamage != (int)tmp[i]) {
	    			  this.isDamageModified = true;
	    		  }
	    		  if (this.ampDamage != (int)amp[i]){
	    			  this.isBlockModified = true;
	    		  }
	    	  }
			}
			for (int i = 0; i < tmp.length; i++) {
				for (AbstractPower p : ((AbstractMonster)m.get(i)).powers) {
					if ((!((AbstractMonster)m.get(i)).isDying) && (!((AbstractMonster)m.get(i)).isEscaping)) {
						tmp[i] = p.atDamageFinalReceive(tmp[i], this.damageTypeForTurn);
						amp[i] = p.atDamageFinalReceive(amp[i], this.damageTypeForTurn);
					}
				}
			}
			for (int i = 0; i < tmp.length; i++) {
				if (tmp[i] < 0.0F) {
					tmp[i] = 0.0F;
				}
				if (amp[i] < 0.0F) {
					amp[i] = 0.0F;
				}
			}
			this.multiDamage = new int[tmp.length];
			for (int i = 0; i < tmp.length; i++) {
				this.multiDamage[i] = MathUtils.floor(tmp[i]);
			}
			this.multiAmpDamage = new int[amp.length];
			for (int i = 0; i < amp.length; i++) {
				this.multiAmpDamage[i] = MathUtils.floor(amp[i]);
			}
			this.damage = this.multiDamage[0];
			this.block = this.multiAmpDamage[0];
			/*
			ThMod.logger.info("AmplifiedAttack : done calculateCardDamage (multiple) : baseDamage :"+this.baseDamage +"; damage :"+this.damage+"; ampNumber : "+this.ampNumber+
					"; ampDamage :"+this.ampDamage+"; block :"+this.block);
			ThMod.logger.info("AmplifiedAttack : done calculateCardDamage (multiple) : multiDamage[0] :"+this.multiDamage[0] +
					"; multiAmpDamage[0] :"+this.multiAmpDamage[0]);
			*/
		}
	}

}
