import { Theme } from './tokens';
import { assertContrast } from './a11y';

export type ButtonVariant = 'primary' | 'ghost';

export type ButtonProps = {
  label: string;
  ariaLabel?: string;
  variant?: ButtonVariant;
  disabled?: boolean;
};

export function composeButton(theme: Theme, props: ButtonProps) {
  if (!props.label && !props.ariaLabel) {
    throw new Error('Accessible name required: provide label or ariaLabel');
  }
  const variant = props.variant ?? 'primary';
  const styles =
    variant === 'primary'
      ? {
          background: theme.colors.primary,
          color: theme.colors.onPrimary,
          border: `1px solid ${theme.colors.primary}`
        }
      : {
          background: 'transparent',
          color: theme.colors.text,
          border: `1px solid ${theme.colors.muted}`
        };

  // contrast check against surface
  const ratio = assertContrast(styles.color, variant === 'primary' ? styles.background : theme.colors.surface, 4.5);

  return {
    role: 'button',
    ariaLabel: props.ariaLabel ?? props.label,
    disabled: !!props.disabled,
    styles: {
      ...styles,
      padding: `${theme.spacing(2)} ${theme.spacing(3)}`,
      borderRadius: theme.radius
    },
    meta: { theme: theme.name, variant, contrast: ratio.toFixed(2) }
  };
}

export function renderButton(vnode: ReturnType<typeof composeButton>): string {
  const { styles, ariaLabel, disabled } = vnode;
  const styleStr = Object.entries(styles)
    .map(([k, v]) => `${k}:${v}`)
    .join(';');
  return `<button role="${vnode.role}" aria-label="${ariaLabel}" style="${styleStr}" ${disabled ? 'disabled' : ''}>${ariaLabel}</button>`;
}
