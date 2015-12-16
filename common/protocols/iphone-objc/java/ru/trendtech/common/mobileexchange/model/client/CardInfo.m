#import "CardInfo.h"

@implementation CardInfo

@synthesize id;
@synthesize number;
@synthesize expirationMonth;
@synthesize expirationYear;
@synthesize cvv;

- (void) dealloc {
  [number release];
  [cvv release];
  [super dealloc];
}

@end
