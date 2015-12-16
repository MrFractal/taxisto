#import "CardInfo.h"
#import "NSMutableArray.h"
#import "NSMutableArray.h"

/**
 * Created by max on 06.02.14.
 * 
 * return clients cards info
 * 
 */

@interface CardsResponse : NSObject {
  NSMutableArray * cards;
}

@property(nonatomic, retain, readonly) NSMutableArray * cards;
- (void) init;
@end
