/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.gruppeseks.bodtrd.common.data.entityelements;

/**
 *
 * @author Dzenita Hasic
 */
public class Weapon
{

    private boolean _isAttacking;
    private int _maxAmmunition;
    private int _currentAmmunition;
    private int _maxMagazineSize;
    private int _currentMagazineSize;
    private float _reloadTimeLeft;
    private float _attackSpeed;
    private boolean _isReloading;
    private float _attackCooldown;
    private Damage _attackDamage;

    public float getAttackCooldown()
    {
        return _attackCooldown;
    }

    public void setAttackCooldown(float attackCooldown)
    {
        this._attackCooldown = attackCooldown;
    }

    public boolean isReloading()
    {
        return _isReloading;
    }

    public void setReloading(boolean isReloading)
    {
        this._isReloading = isReloading;
        if (_isReloading)
        {
            setReloadTimeLeft(getReloadSpeed());
        }
    }

    public float getAttackSpeed()
    {
        return _attackSpeed;
    }

    public void setAttackSpeed(float attackSpeed)
    {
        this._attackSpeed = attackSpeed;
    }

    public float getReloadTimeLeft()
    {
        return _reloadTimeLeft;
    }

    public void setReloadTimeLeft(float reloadTimeLeft)
    {
        this._reloadTimeLeft = reloadTimeLeft;
    }

    public float getReloadSpeed()
    {
        return _reloadSpeed;
    }

    public void setReloadSpeed(float reloadSpeed)
    {
        this._reloadSpeed = reloadSpeed;
    }
    private float _reloadSpeed;

    public int getMaxAmmunition()
    {
        return _maxAmmunition;
    }

    public void setMaxAmmunition(int maxAmmunition)
    {
        this._maxAmmunition = maxAmmunition;
    }

    public int getCurrentAmmunition()
    {
        return _currentAmmunition;
    }

    public void setCurrentAmmunition(int currentAmmunition)
    {
        this._currentAmmunition = currentAmmunition;
    }

    public int getMaxMagazineSize()
    {
        return _maxMagazineSize;
    }

    public void setMaxMagazineSize(int maxMagazineSize)
    {
        this._maxMagazineSize = maxMagazineSize;
    }

    public int getCurrentMagazineSize()
    {
        return _currentMagazineSize;
    }

    public void setCurrentMagazineSize(int currentMagazineSize)
    {
        this._currentMagazineSize = currentMagazineSize;
    }

    public boolean isAttacking()
    {
        return _isAttacking;
    }

    public void setAttacking(boolean isAttacking)
    {
        this._isAttacking = isAttacking;
    }

    public Damage getAttackDamage() 
    {
        return _attackDamage;
    }

    public void setAttackDamage(Damage attackDamage) 
    {
        this._attackDamage = attackDamage;
    }
}
